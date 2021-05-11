package com.example.cuidatucarro.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cuidatucarro.R
import com.example.cuidatucarro.viewmodel.FirestoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_manual.*
import kotlinx.android.synthetic.main.activity_main.*

class RegistroManualActivity : AppCompatActivity() {

    private lateinit var viewModel: FirestoreViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var progressbar: ProgressBar
    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtNombre = findViewById(R.id.txtNombre)
        txtApellido = findViewById(R.id.txtApellido)
        txtCorreo = findViewById(R.id.txtEmail)
        txtContrasena = findViewById(R.id.txtPassword)
        progressbar = findViewById(R.id.progressBar)
        //Instancia de viewModel
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)
        crearUsuario()

    }

    fun crearUsuario() {

        btn_Crear_Cuenta.setOnClickListener{
        progressbar.visibility = View.VISIBLE
            val nombre = txtNombre.text.toString().trim()
            val apellido = txtApellido.text.toString().trim()
            val correoElectronico: String = txtCorreo.text.toString().trim()
            val contrasena: String = txtContrasena.text.toString().trim()

            if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(correoElectronico) && !TextUtils.isEmpty(contrasena)) {
                if (contrasena.length >= 8 ) {

                    auth.createUserWithEmailAndPassword(correoElectronico,contrasena)
                        .addOnCompleteListener(this) {
                                task ->
                            if(task.isSuccessful){
                                val user:FirebaseUser?=auth.currentUser

                                viewModel.crearUsuario(nombre, apellido, user?.uid.toString())

                                enviarCorreo(user)

                                progressbar.visibility = View.GONE

           /*                     startActivity(Intent(this,LoginActivity::class.java))
                                startActivity(Intent(this,LoginActivity::class.java))*/

                       /*         finish()*/
                            }else{
                                Toast.makeText(this, "Ocurrio un problema al intentar registrar el nuevo usuario", Toast.LENGTH_SHORT).show()
                            }
                        }
                }else{
                    Toast.makeText(this, "La contraseña debe de tener mínimo 8 caracteres", Toast.LENGTH_SHORT).show()
                    progressbar.visibility = View.GONE
                }

            }else{

               /* AlertEmail()*/

                if (nombre.isEmpty()){
                    txtNombre.setError("El nombre es un campo Obligatiorio para completar")
                }

                if (apellido.isEmpty()){
                    txtApellido.setError("El Apellido es un campo Obligatiorio para completar")
                }

                if (correoElectronico.isEmpty()){
                    txtCorreo.setError("El Email es un campo Obligatiorio para completar")
                }

                if (contrasena.isEmpty()){
                    txtContrasena.setError("La contraseña es un campo Obligatiorio para completar")
                    progressbar.visibility = View.GONE
                }
                /*Toast.makeText(this, "Se deben de completar todos los datos solicitados", Toast.LENGTH_SHORT).show()*/
            }

        }
        progressbar.visibility = View.GONE
    }

    private fun enviarCorreo(user: FirebaseUser?){
        user?.sendEmailVerification()
                ?.addOnCompleteListener(this){
                    task ->
                    if (task.isComplete){
                       /* Toast.makeText(this, "Email de confirmación de Registro enviado", Toast.LENGTH_SHORT).show()*/
                        AlertEmail()
                        //startActivity(Intent(this, Login_manual::class.java))
                    }else{
                        Toast.makeText(this, "Ocurrió un problema al enviar email de confirmación de Registro", Toast.LENGTH_SHORT).show()
                        progressbar.visibility = View.GONE
                    }

                }
    }

    private fun AlertEmail(){
        var dialogBuilder = AlertDialog.Builder(this)
        val layoutView = layoutInflater.inflate(R.layout.activity_aler_email_enviado, null)

        val dialogButton =
            layoutView.findViewById<Button>(R.id.btnOk)
        dialogBuilder.setView(layoutView)
        dialogBuilder.setCancelable(false)
        var alertDialog = dialogBuilder.create()
        alertDialog.show()
        dialogButton.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
    }


}
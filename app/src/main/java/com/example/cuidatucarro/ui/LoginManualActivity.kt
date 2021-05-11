package com.example.cuidatucarro.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.cuidatucarro.R
import com.example.cuidatucarro.viewmodel.FirestoreViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_manual.*
private lateinit var txtuser: EditText
private lateinit var txtpassword: EditText
private lateinit var progressbar: ProgressBar
private lateinit var auth: FirebaseAuth
private lateinit var viewModel: FirestoreViewModel


class LoginManualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_manual)

        progressbar = findViewById(R.id.progressBar)
        txtuser = findViewById(R.id.txtUser)
        txtpassword = findViewById(R.id.txtPassword)
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)

        funcionBotones()
    }

    fun funcionBotones(){

        btnRegistrarse.setOnClickListener{

            startActivity(Intent(this,RegistroManualActivity::class.java))
        }

        btnIngresar.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            loginUser()
            progressBar.visibility = View.GONE
    }

        btnOlvideContrasena.setOnClickListener{
            startActivity(Intent(this,OlvideContrasenaActivity::class.java))
        }

    }

    private fun loginUser(){
        val user:String= txtuser.text.toString()
        val password:String= txtpassword.text.toString()

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {

            auth.signInWithEmailAndPassword(user,password)
                .addOnCompleteListener(this){
                        task ->
                    if(task.isSuccessful) {
                        val datosUsuario = FirebaseAuth.getInstance().currentUser

                        var codError:Long = 0

                        codError = viewModel.autentificacionCorreo(datosUsuario)

                        if (codError>0){
                            var dato:String = "${datosUsuario!!.uid}"
                            val intent = Intent(this, MenuPrincipal::class.java)
                            intent.putExtra("iduser",dato)
                            startActivity(intent)

                            finish()
                        }else{
                            Toast.makeText(this, "No se ha realizado la Autentificación", Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(this, "Correo Electrónico o Contraseña invalidas, Error en la autentificación", Toast.LENGTH_LONG).show()
                    }
                }

        }else{
            if (user.isEmpty()){
                txtuser.setError("Email es campo obligatorio para completar")
            }

            if (password.isEmpty()){
                txtpassword.setError("Contraseña es campo obligatorio para completar")
            }

        }

    }

    //Tocar el boton back
    override fun onBackPressed(){
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }



}
package com.example.cuidatucarro.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.cuidatucarro.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_olvide_contrasena.*

class OlvideContrasenaActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var auth:FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvide_contrasena)

        txtEmail = findViewById(R.id.txtEmail)
        auth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressBar)

        botones()
    }

    fun botones(){

        btnenviar.setOnClickListener{
           send()
        }

    }

    fun send(){
        val email = txtEmail.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                        task ->
                    if(task.isSuccessful){
                        progressBar.visibility = View.VISIBLE
                        Toast.makeText(this,"Se realizo el envío de un email", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginManualActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Error al enviar el Email", Toast.LENGTH_LONG).show()
                    }
                }

        }else{
            txtEmail.setError("Debe completar el campo solicitado")
        }


    }


}
package com.example.cuidatucarro.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.cuidatucarro.R
import com.example.cuidatucarro.viewmodel.FirestoreViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var progressbar: ProgressBar

    companion object {
        private const val RC_SIGN_IN = 423
    }

    private val authUser: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var viewModel: FirestoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {


        setTheme(R.style.AppTheme)
        Thread.sleep(2000)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)
        progressbar = findViewById(R.id.progressBar)

        goTo()
        googleLogin()
        botones()

    }

    fun botones(){
/*        btn_login_google.setOnClickListener{
            progressbar.visibility = View.VISIBLE
            googleLogin()
    }*/

        btn_login_manual.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            startActivity(Intent(this, LoginManualActivity::class.java))
            finish()
        }
    }

    fun goTo(){

        if(authUser.currentUser != null){

            var codError:Long

            codError = viewModel.autentificacionCorreo(authUser.currentUser)

            if (codError>0){
                val datosUsuario = FirebaseAuth.getInstance().currentUser
         /*       var dato:String = "${datosUsuario!!.uid}"*/

              /*  viewModel.obtenerDatosUser(dato)*/

/*                val intent = Intent(this, MenuPrincipal::class.java)*/
            /*    intent.putExtra("iduser",dato)*/
                startActivity(Intent(this, MenuPrincipal::class.java))
            finish()
            }
        }
    }

    fun googleLogin(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())
        btn_login_google.setOnClickListener{
            progressbar.visibility = View.VISIBLE
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .build(),
                RC_SIGN_IN)
            progressbar.visibility = View.GONE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
           /*     val user = FirebaseAuth.getInstance().currentUser*/
                /*var codError:Long*/

                //var dato:String = "${user!!.displayName}"
      /*          var dato:String = user?.uid.toString()*/
/*                val intent = Intent(this, MenuPrincipal::class.java)*/
                /*intent.putExtra("iduser",dato)*/
                /*startActivity(intent)*/
                startActivity(Intent(this, MenuPrincipal::class.java))
                finish()

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this,"Ocurrio un problema ${response!!.error!!.errorCode}", Toast.LENGTH_SHORT ).show()
                /*startActivity(Intent(this, LoginActivity::class.java))*/

            }
        }

    }

    //Tocar el boton back
    override fun onBackPressed(){

        val builder = AlertDialog.Builder(this).create()
        builder.setTitle("Saliendo de Cuida tu carro")
        builder.setMessage("¿Esta seguro en salir de la aplicación?")
        builder.setButton(AlertDialog.BUTTON_POSITIVE,"Salir"){
                _: DialogInterface?, _: Int ->
            finish()
            MenuPrincipal().finish()
            LoginActivity().finish()
        }
        builder.setButton(AlertDialog.BUTTON_NEGATIVE,"No"){
                _: DialogInterface?, _: Int ->
            builder.cancel()
        }
        builder.show()
    }




}
package com.example.cuidatucarro.viewmodel

import android.widget.Toast
import com.example.cuidatucarro.domain.FirestoreUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.cuidatucarro.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class FirestoreViewModel : ViewModel() {

    val firestoreUseCase = FirestoreUseCase()
    //Creación de metodo para recibir variables
    fun crearUsuario(nombre:String,apellido:String, idUsuario:String){
        firestoreUseCase.setearUsuarioFirestore(nombre,apellido,idUsuario)
    }

    fun obtenerDatosUser(idUser:String){
       firestoreUseCase.obtenerDatosUsuarioFirestore(idUser)
    }

     fun autentificacionCorreo(user:FirebaseUser?) :Long{
        var codError:Long
        if (user?.isEmailVerified()!!){
            codError = 1
        }else {
            codError = -1
        }
        return codError
    }




}
package com.example.cuidatucarro.domain

import android.net.Uri
import com.example.cuidatucarro.data.repo.FirebaseRepo
import com.example.cuidatucarro.objetos.Autos

class FirestoreUseCase {

    //Instancia
    val repo = FirebaseRepo()

    //Clase para setear datos
    fun setearUsuarioFirestore(nombre:String,apellido:String,idUser:String){
        repo.setUserData(nombre,apellido,idUser)
    }

    fun obtenerDatosUsuarioFirestore(idUser: String){
        repo.obtenerUserData(idUser)
    }

    fun agregarNuevoVehiculo(idUsuario:String, patente:String, marca:String, modelo:String, km:Long, trasmi:String, image:String, uriPhoto:String){
        repo.agregarNuevoVehiculo(idUsuario,patente,marca,modelo,km,trasmi,image,uriPhoto)
    }

    fun actualizadarDatosVehiculo(idAuto:String, idUsuario:String,marca:String, modelo:String, km:Long, trasmi:String, image:String, uriPhoto:String){
        repo.actualizarDatosVehiculo(idAuto,idUsuario,marca,modelo,km,trasmi,image,uriPhoto)
    }

    fun eliminarVehiculo(uriImgage:String,idvehiculo: String){
        repo.eliminarVehiculo(uriImgage, idvehiculo)
    }


}
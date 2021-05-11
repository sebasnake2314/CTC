package com.example.cuidatucarro.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.example.cuidatucarro.data.repo.RepoAuto
import com.example.cuidatucarro.domain.FirestoreUseCase
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.objetos.mantenientosAutos

class AutoViewModel:ViewModel() {

    val firestoreUseCase = FirestoreUseCase()
    private val repo = RepoAuto()

    fun agregarNuevoVehocilo(idUsuario:String, patente:String, marca:String, modelo:String, km:Long, trasmi:String, image:String, uriPhoto:String){
        firestoreUseCase.agregarNuevoVehiculo(idUsuario,patente,marca,modelo,km,trasmi,image,uriPhoto)
    }

    fun traerDatosVehiculos(idUsuario: String):LiveData<MutableList<Autos>>{
        val mutableData = MutableLiveData<MutableList<Autos>>()
        repo.getDataAutos(idUsuario).observeForever{ autosList->
            mutableData.value = autosList
        }
        return mutableData
    }

    fun actualizarDatosVehiculos(idAuto:String, idUsuario:String,marca:String, modelo:String, km:Long, trasmi:String, image:String, uriPhoto:String){
        firestoreUseCase.actualizadarDatosVehiculo(idAuto,idUsuario,marca,modelo,km,trasmi,image,uriPhoto)
    }

    fun eliminarVehículo(uriImgage:String,idvehiculo: String){
        repo.eliminarVehiculo(uriImgage,idvehiculo)
    }

    fun traerFechasServicios(id_auto:String, desVeh:String, patente:String):LiveData<MutableList<mantenientosAutos>>{
        val mutableData = MutableLiveData<MutableList<mantenientosAutos>>()
        repo.traerMantenimientos(id_auto,desVeh,patente).observeForever{ autosList->
            mutableData.value = autosList
        }
        return mutableData
    }

}
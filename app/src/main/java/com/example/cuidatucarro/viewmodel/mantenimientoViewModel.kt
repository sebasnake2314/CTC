package com.example.cuidatucarro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cuidatucarro.data.repo.RepoMantenimiento
import com.example.cuidatucarro.objetos.mantenimientoMotor
import com.example.cuidatucarro.objetos.tipoMatenimientoMotor

class mantenimientoViewModel: ViewModel()  {
    private val repoMatenimiento = RepoMantenimiento()

    fun traerTipoMantenimientoMotor(tipoMantenimiento:String): LiveData<MutableList<tipoMatenimientoMotor>> {
        val mutableData = MutableLiveData<MutableList<tipoMatenimientoMotor>>()
        repoMatenimiento.traerMantenimientosMotor(tipoMantenimiento).observeForever{listaMantenimiento->
            mutableData.value = listaMantenimiento
        }
        return mutableData
    }

    fun agregarMantenimientoMotor(mantenimiento:mantenimientoMotor){
            repoMatenimiento.agregarMantenimiento(mantenimiento)
    }

}
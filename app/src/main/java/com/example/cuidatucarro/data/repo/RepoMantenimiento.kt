package com.example.cuidatucarro.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.objetos.mantenimientoMotor
import com.example.cuidatucarro.objetos.tipoMatenimientoMotor
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp

class RepoMantenimiento {
    val db = FirebaseFirestore.getInstance()

    fun traerMantenimientosMotor(tipoMantenimiento:String): LiveData<MutableList<tipoMatenimientoMotor>>{
        val mutableData = MutableLiveData<MutableList<tipoMatenimientoMotor>>()
        FirebaseFirestore.getInstance().collection("tb_mant_motor")
            .whereEqualTo("tipoMant",tipoMantenimiento)

            .get().addOnSuccessListener { result ->
            val listData = mutableListOf<tipoMatenimientoMotor>()
            for(document in result ){
                val tipoMantenimiento = document.id.toString()

                val mantenimiento = tipoMatenimientoMotor(tipoMantenimiento!!)
                listData.add(mantenimiento)
            }
            mutableData.value = listData
        }
        return mutableData
    }

    fun agregarMantenimiento(mantenimientos:mantenimientoMotor){

        val userHashMap = hashMapOf(
            "mant_id_auto" to mantenimientos.idVehiculo,
            "mant_tipo" to mantenimientos.servicio,
            "mant_fecha" to mantenimientos.fecha,
            "mant_kilometraje" to mantenimientos.kilimetraje,
            "mant_lugar" to mantenimientos.lugarMantenimiento,
            "mant_tipo_aceite" to mantenimientos.tipoAceite,
            "mant_marca_filtro" to mantenimientos.MarcaFiltro
         )

        db.collection("tb_mantenimientos")
            .add(userHashMap).addOnCompleteListener{
                if(it.isSuccessful){
                    //Mensaje Exitoso
                }else{
                    //Mensaje Error
                }
            }

        actualizarKilometraje(mantenimientos.idVehiculo, mantenimientos.kilimetraje)

    }

    fun actualizarKilometraje(idAuto:String, Kilometraje:Int){
        db.collection("tb_vehiculo_usuario").document(idAuto).update(mapOf(
            "aut_kilometraje_i" to Kilometraje
        ))
    }
}
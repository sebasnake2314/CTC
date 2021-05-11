package com.example.cuidatucarro.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.objetos.mantenientosAutos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RepoAuto {

    //val db = FirebaseFirestore.getInstance()

    fun getDataAutos(idUsuario:String):LiveData<MutableList<Autos>>{
        val mutableData = MutableLiveData<MutableList<Autos>>()
        FirebaseFirestore.getInstance().collection("tb_vehiculo_usuario")
            .whereEqualTo("usu_id_usuario_i",idUsuario)
            .get().addOnSuccessListener {result->
            val listData = mutableListOf<Autos>()
            for(document in result ){
                val idVeh = document.id.toString()
                val imageUrl = document.getString("aut_image")
                val patente = document.getString("aut_patente_c")
                val marca = document.getString("aut_marca_c")
                val modelo = document.getString("aut_modelo_c")
                val km = document.getLong("aut_kilometraje_i")?.toInt()
                val trasminision = document.getString("aut_tipo_trasmision_c")
                var uri_auto = document.getString("auto_uri_foto")
                if (uri_auto == null){
                    uri_auto = ""
                }
                val autos = Autos(idVeh!!,imageUrl!!, patente!!,marca!!,modelo!!,km!!.toLong(),trasminision!!,uri_auto!!)
                listData.add(autos)
            }
            mutableData.value = listData
        }
        return mutableData
    }

    fun eliminarVehiculo(uriImgage:String,idvehiculo: String){

        //1- se elimina el archivo de imagen del vehículo
        val  ref= FirebaseStorage.getInstance().getReference("/images/$uriImgage" )
        ref.delete().addOnSuccessListener{}

        //2 - Se eliminar Mantenimientos relacionado con el vehículo
        FirebaseFirestore.getInstance().collection("tb_mantenimientos")
            .whereEqualTo("mant_id_auto",idvehiculo).get().addOnSuccessListener { result->
                for(document in result){
                    FirebaseFirestore.getInstance().collection("tb_mantenimientos").document(document.id.toString()).delete()
                }
            }

        //3-Se elimina el vehículo relacionado con el usuario
        FirebaseFirestore.getInstance().collection("tb_vehiculo_usuario").document(idvehiculo).delete()
    }

    fun traerMantenimientos(idVehiculo:String, desVeh:String, patente:String):LiveData<MutableList<mantenientosAutos>>{
        val mutableData = MutableLiveData<MutableList<mantenientosAutos>>()
        FirebaseFirestore.getInstance().collection("tb_mantenimientos")
            .whereEqualTo("mant_id_auto",idVehiculo)
            .get().addOnSuccessListener {result->
                val listData = mutableListOf<mantenientosAutos>()
                for(document in result ){
                    val idMant = document.id.toString()
                    val fecha = document.getString("mant_fecha")
                    val tipoMant = document.getString("mant_tipo")
                    val kilometraje = document.get("mant_kilometraje")
                    val mant = mantenientosAutos(fecha!!,tipoMant!!, desVeh!!, patente!!,kilometraje!!.toString().toLong(),idMant!!)
                    listData.add(mant)
                }
                mutableData.value = listData
            }
        return mutableData
    }


}
package com.example.cuidatucarro.data.repo

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.cuidatucarro.data.model.Usuario
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FirebaseRepo {

    val db = FirebaseFirestore.getInstance()
    val generateUsuarioList = Resource.Success(
        listOf(
    Usuario("Solid","Snake")
    ))

    //Cargar nuevo usuario
    fun setUserData(nombre:String,apellido:String,idUser:String){
        val userHashMap = hashMapOf(
            "Nombre" to nombre,
            "Apellido" to apellido
        )
        db.document("usuarios/"+idUser)
            .set(userHashMap).addOnCompleteListener{
                if(it.isSuccessful){
                    //Mensaje Exitoso
                }else{
                    //Mensaje Error
                }
            }
    }

    //Obtener datos de usuario
    fun obtenerUserData(idUser:String){
        var listaUsuario = mutableListOf<Usuario>()
        db.collection("usuarios").document(idUser).get().addOnSuccessListener {documento ->

            if(documento.exists()){
                val usuario = documento.toObject(Usuario::class.java)
                if (usuario != null) {
                    listaUsuario.add(usuario)
                    Log.d("Datos: " , "$listaUsuario"  )
                }

            }else{
                Log.d("Datos doc: " ,"No existe")
            }
        }
    }

    //Agregar vehículo nuevo
    fun agregarNuevoVehiculo(idUsuario:String, patente:String, marca:String, modelo:String, km:Long, trasmi:String, image:String,uriPhoto:String){

          val userHashMap = hashMapOf(
              "usu_id_usuario_i" to idUsuario,
              "aut_image" to image,
              "aut_patente_c" to patente,
              "aut_marca_c" to marca,
              "aut_modelo_c" to modelo,
              "aut_kilometraje_i" to km,
              "aut_fecha_alta_f" to Timestamp(System.currentTimeMillis()),
              "aut_tipo_trasmision_c" to trasmi,
              "auto_uri_foto" to uriPhoto)

          db.collection("tb_vehiculo_usuario")
              .add(userHashMap).addOnCompleteListener{
                  if(it.isSuccessful){
                      //Mensaje Exitoso
                  }else{
                      //Mensaje Error
                  }
      }
    }

    //Actualizar datos de vehiculo
    fun actualizarDatosVehiculo(idAuto:String, idUsuario:String,marca:String, modelo:String, km:Long, trasmi:String, image:String, uriPhoto:String){

        db.collection("tb_vehiculo_usuario").document(idAuto).update(mapOf(
            "aut_image" to image,
            "aut_marca_c" to marca,
            "aut_modelo_c" to modelo,
            "aut_kilometraje_i" to km,
            "aut_tipo_trasmision_c" to trasmi,
            "auto_uri_foto" to uriPhoto

        ))
    }

    //Eliminar vehiculo
    fun eliminarVehiculo(uriImgage:String,idvehiculo: String){

        //1- se elimina el archivo de imagen del vehículo
        val  ref= FirebaseStorage.getInstance().getReference("/images/$uriImgage" )

        ref.delete().addOnSuccessListener {
            //Al eliminar archivo de imagen del storage se procede a eliminar los datos
            db.collection("tb_vehiculo_usuario").document(idvehiculo).delete()

        }
    }

}
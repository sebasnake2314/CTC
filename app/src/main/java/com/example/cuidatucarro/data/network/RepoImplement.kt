package com.example.cuidatucarro.data.network


import android.net.Uri
import androidx.core.net.toUri
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*


class RepoImplement:IRepo {

    override suspend fun getDatosUsuarioRepo(idUsuario:String): Resource<String>{
     val db = FirebaseFirestore.getInstance()
         .collection("usuarios").document(idUsuario).get().await()
       val nombre = db.getString("Nombre")
       val apellido = db.getString("Apellido")

        return Resource.Success("$nombre $apellido" )
    }

    override suspend fun getDatosVehículo(): Resource<List<Autos>> {
        TODO("Not yet implemented")
    }

    override suspend fun geturlimagen(image: Uri?): Resource<String> {
        var url:String = ""
        var selectedPhotoUri: Uri? = image


        val filename = UUID.randomUUID().toString()
        /*val  ref=  FirebaseStorage.getInstance().getReference("/images/$filename")*/
        val  ref=  FirebaseStorage.getInstance()

            .getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {

            ref.downloadUrl.addOnCompleteListener{
                url = it.toString()
            }
        }

            .addOnSuccessListener {

           if (ref.downloadUrl.isComplete) {
               url = it.toString()
           }

            if(ref.downloadUrl.isSuccessful){

/*                ref.downloadUrl.addOnCompleteListener(){
                    if (it.isSuccessful){

                    }
                }*/
            }
        }

      /*      .addOnSuccessListener{*/
                /*ref.downloadUrl.await()*/

                /*ref.downloadUrl.await()*/

 /*               ref.downloadUrl.addOnSuccessListener {
                        url = it.toString()

                    }*/


                /*}*/
        /*    }*/

        return Resource.Success(url)
    }
}


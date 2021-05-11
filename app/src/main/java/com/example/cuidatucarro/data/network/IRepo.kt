package com.example.cuidatucarro.data.network

import android.net.Uri
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.vo.Resource

interface IRepo {
    suspend fun getDatosUsuarioRepo(idUsuario:String): Resource<String>

    suspend fun getDatosVehículo():Resource<List<Autos>>

    suspend fun geturlimagen(image:Uri?):Resource<String>

}


package com.example.cuidatucarro.domain

import android.net.Uri
import com.example.cuidatucarro.vo.Resource

interface IUseCase {

    suspend fun getDatosUsuario(idUsuario:String):Resource<String>

    suspend fun geturlimagen(image:Uri?):Resource<String>

}
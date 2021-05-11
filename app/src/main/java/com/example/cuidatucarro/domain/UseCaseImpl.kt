package com.example.cuidatucarro.domain

import android.net.Uri
import com.example.cuidatucarro.data.network.IRepo
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.vo.Resource

class UseCaseImpl(private val repo:IRepo):IUseCase {

    override suspend fun getDatosUsuario(idUsuario:String): Resource<String> = repo.getDatosUsuarioRepo(idUsuario)

    override suspend fun geturlimagen(image:Uri?):Resource<String> = repo.geturlimagen(image)

}
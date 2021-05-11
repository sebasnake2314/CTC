package com.example.cuidatucarro.domain

import com.example.cuidatucarro.data.model.Usuario
import com.example.cuidatucarro.data.repo.RepoAuto
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.vo.Resource

class RepoImple(private val dataSourse: RepoAuto):Repo {
    override fun getDatosUsuario(): Resource<List<Usuario>> {
        TODO("Not yet implemented")
    }

    override fun getlistaautos(): Resource<List<Autos>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNombreUsuario(): Resource<Int> {
        TODO("Not yet implemented")
    }
}
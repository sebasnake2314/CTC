package com.example.cuidatucarro.data.repo

import com.example.cuidatucarro.data.model.Usuario
import com.example.cuidatucarro.domain.Repo
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.vo.Resource

class RepoImpl(private val FirebaseRepo: FirebaseRepo) : Repo {

    override fun getDatosUsuario(): Resource<List<Usuario>> {
        return FirebaseRepo.generateUsuarioList
    }

    override fun getlistaautos(): Resource<List<Autos>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNombreUsuario(): Resource<Int> {
        TODO("Not yet implemented")
    }
}
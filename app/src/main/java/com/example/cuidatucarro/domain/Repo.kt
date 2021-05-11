package com.example.cuidatucarro.domain

import com.example.cuidatucarro.data.model.Usuario
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.vo.Resource

interface Repo {

    fun getDatosUsuario():Resource<List<Usuario>>

    fun getlistaautos():Resource<List<Autos>>

    suspend fun getNombreUsuario():Resource<Int>
}
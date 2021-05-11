package com.example.cuidatucarro.ui.mantenimiento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cuidatucarro.R
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.objetos.tipoMatenimientoMotor
import com.example.cuidatucarro.recyclers.MainAdapter
import com.example.cuidatucarro.viewmodel.mantenimientoViewModel

class nuevoMantenimientoMotor : Fragment() {
    private var ListaMantenimientoMotor = mutableListOf<tipoMatenimientoMotor>()
    private lateinit var auto: Autos
    private val viewModel by lazy { ViewModelProvider(this).get(mantenimientoViewModel::class.java) }
    private lateinit var adapter : MainAdapter
    private lateinit var mantenimientosMotor : Array<String>
    var savedia = 0
    var savemes = 0
    var saveanio = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let{
            auto = it.getParcelable("auto")!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nuevo_mantenimiento_motor, container, false)
    }

}
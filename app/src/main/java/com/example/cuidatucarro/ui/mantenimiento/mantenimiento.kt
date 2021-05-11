package com.example.cuidatucarro.ui.mantenimiento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cuidatucarro.R
import com.example.cuidatucarro.objetos.Autos
import kotlinx.android.synthetic.main.fragment_mantenimiento.*

class mantenimiento : Fragment() {

    private lateinit var auto: Autos

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
        return inflater.inflate(R.layout.fragment_mantenimiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placaVehiculo.text = auto.aut_patente_c
        datosVehiculo.text = "${auto.aut_marca_c} ${auto.aut_modelo_c}"
        Glide.with(this).load(auto.aut_image).into(image_auto)

        btnmotor.setOnClickListener {
           val bundle = Bundle()
            bundle.putParcelable("auto", auto)
           findNavController().navigate(R.id.agregarMantenimientoMotor, bundle)
        }

    }



}
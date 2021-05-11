package com.example.cuidatucarro.ui.autos

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cuidatucarro.R
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.recyclers.MainAdapter
import com.example.cuidatucarro.viewmodel.AutoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_auto.*
import kotlinx.android.synthetic.main.tarjeta_veh.view.*


class AutosFragment : Fragment(),MainAdapter.OnAutoClickListener {
    private lateinit var floatingActionButton: FloatingActionButton
    private var listVehículos = mutableListOf<Autos>()
    var esconderBar:Boolean = false
    var posicionrow:Int = 0

    private lateinit var adapter :MainAdapter
    private val viewModel by lazy {ViewModelProvider(this).get(AutoViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_auto, container, false)
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MainAdapter(view.context, this)
        recyclerAutos.layoutManager = LinearLayoutManager(this.activity)
        recyclerAutos.adapter = adapter
        recyclerAutos.addItemDecoration(
            DividerItemDecoration(
                this.activity,
                DividerItemDecoration.VERTICAL
            )
        )
        observeData()

        floatingActionButton = view.findViewById(R.id.btnadd)

    btnbar.setOnClickListener{
        if (esconderBar){
            floatingActionButton.show()
            esconderBar  = false
        }else{
            floatingActionButton.hide()
            esconderBar = true
        }
    }

        btnadd.setOnClickListener{
            coordinatorLayout.visibility = View.VISIBLE

            val autoNull: Autos? = Autos("", "", "", "", "", 0, "")
            val bundle = Bundle()
            bundle.putParcelable("auto", autoNull)

            findNavController().navigate(R.id.fragemtagregarauto, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

    fun observeData(){

        shimmer_view_container.startShimmer()
        val datosUsuario = FirebaseAuth.getInstance().currentUser
        viewModel.traerDatosVehiculos(datosUsuario!!.uid).observe(viewLifecycleOwner, Observer {
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
            coordinatorLayout.visibility = View.VISIBLE
            listVehículos = it

        })

    }

    override fun onImageClick(autImage: String) {
        val intent = Intent(context, ImagenVehiculo::class.java)
        intent.putExtra("imageURL", autImage)
        startActivity(intent)

    }

    override fun onItemClik(auto: Autos, posicion: Int) {
        posicionrow = posicion
        showAlertDialogSuccess(auto)
    }


    private fun showAlertDialogSuccess(auto: Autos) {
        var dialogBuilder = AlertDialog.Builder(activity)
        val layoutView = layoutInflater.inflate(R.layout.tarjeta_veh, null)

        layoutView.txtPatente.text = auto.aut_patente_c

        layoutView.txtMarcaModelo.text = "${auto.aut_marca_c} ${auto.aut_modelo_c}"

        Glide.with(layoutView).load(auto.aut_image).into(layoutView.imaVeh)

        val btnMantenimiento = layoutView.findViewById<Button>(R.id.btnMantenimientos)
        val btnModificar = layoutView.findViewById<Button>(R.id.btnEdit)
        val btnEliminar = layoutView.findViewById<Button>(R.id.btnDetele)

        val dialogButton =
            layoutView.findViewById<Button>(R.id.btnDetele)
        dialogBuilder.setView(layoutView)
        var alertDialog = dialogBuilder.create()

        alertDialog.show()

        dialogButton.setOnClickListener {
        }


        //Ir a mantenimientos
        btnMantenimiento.setOnClickListener{
            alertDialog.dismiss()
            val bundle = Bundle()
            bundle.putParcelable("auto", auto)
           findNavController().navigate(R.id.Mantenimientos, bundle)
        }

        //Modificar datos de Vehículos
        btnModificar.setOnClickListener{

        alertDialog.dismiss()
        val bundle = Bundle()
            bundle.putParcelable("auto", auto)
        findNavController().navigate(R.id.fragemtagregarauto, bundle)

        }

        //Eliminar Vehículo de la lista
        btnEliminar.setOnClickListener{

            //Se llama el ShowAlert de eliminación de vehículo para confirmar la eliminación.
            showAlertDeleteAuto(auto.auto_uri_foto, auto.id_auto)
            observeData()
            alertDialog.dismiss()
        }
    }

    private fun showAlertDeleteAuto(uriAuto: String, idAuto: String) {
        var dialogBuilder = android.app.AlertDialog.Builder(activity).create()
        dialogBuilder.setTitle("Eliminando Vehículo")
        dialogBuilder.setMessage("¿Quiere eliminar este vehículo? Todos los datos de este seran eliminados permanentemente.")
        dialogBuilder.setButton(
            androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE,
            "Cancelar"
        ) { _: DialogInterface?, _: Int ->
            dialogBuilder.cancel()
        }
        dialogBuilder.setButton(
            androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE,
            "Eliminar"
        ) { _: DialogInterface?, _: Int ->

            //Ejecución de proceso de eliminación de vehículo
            viewModel.eliminarVehículo(uriAuto, idAuto)

            //Refrescar Fragment
            val ft = requireFragmentManager().beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false)
            }
            ft.detach(this).attach(this).commit()

        }
        dialogBuilder.show()
    }



}



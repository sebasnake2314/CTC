package com.example.cuidatucarro.ui.home


import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cuidatucarro.R
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.objetos.mantenientosAutos
import com.example.cuidatucarro.recyclers.MainMantenimientos_vehi
import com.example.cuidatucarro.viewmodel.AutoViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_auto.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.shimmer_view_container
import sun.bob.mcalendarview.MCalendarView
import sun.bob.mcalendarview.MarkStyle
import sun.bob.mcalendarview.listeners.OnDateClickListener
import sun.bob.mcalendarview.vo.DateData
import java.util.*


class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var listVehículos = mutableListOf<Autos>()
    private var listMantenimientos = mutableListOf<mantenientosAutos>()
    private var listarecyclemant = mutableListOf<mantenientosAutos>()
    private lateinit var adapter :MainMantenimientos_vehi
    private val viewModel by lazy { ViewModelProvider(this).get(AutoViewModel::class.java) }
    private var spinner:Spinner ? = null
    private var arrayAdapter:ArrayAdapter<String> ? = null
    private var listrecyclemant = mutableListOf<mantenientosAutos>()
    private var itemList =  arrayListOf("Todos")
    private var fechaSelect: String = ""
    val datosUsuario = FirebaseAuth.getInstance().currentUser
    val fecha_now_new: DateData = DateData(1900, 1, 1)
    var fechaExistente:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_home, container, false)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmer_view_container.startShimmer()

        calendar.markedDates.removeAdd()
        listMantenimientos.clear()

        traervehiculos()

        recyclerMantenimientos.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        btnaddmantenimiento.setOnClickListener {
            popTransmision()
        }

        calendar.setOnDateClickListener(object : OnDateClickListener() {
            override fun onDateClick(view: View, date: DateData) {


                var existeFecha: Int = 0
                var fechaEncontrada: DateData = DateData(1900, 1, 1)


                // for (i in 0 until  calendar.markedDates.all.size){
                for (i in 0 until listMantenimientos.size) {
                    //var fecha= DateData(calendar.markedDates.all[i].year,calendar.markedDates.all[i].month,calendar.markedDates.all[i].day)

                    //var fecha =


                    val source = listMantenimientos[i].fecha

                    val sourceSplit: List<String> = source.split(" / ")

                    val anio: Int = sourceSplit.get(2).toInt()
                    val mes: Int = sourceSplit.get(1).toInt()
                    val dia: Int = sourceSplit.get(0).toInt()

                    var fecha= DateData(anio,mes,dia)


                    if (date == fecha) {
                        existeFecha = 1
                        fechaEncontrada = fecha
                    }
                }

                if (existeFecha == 0) {

                    if (date != fecha_now_new) {
                        calendar.markDate(date.year, date.month, date.day)
                    }

                    if (fecha_now_new != DateData(1900, 1, 1) && date !=fecha_now_new) {
                    //if (fechaExistente == 1) {
                        calendar.unMarkDate(
                            fecha_now_new.year,
                            fecha_now_new.month,
                            fecha_now_new.day
                        )
                    }

                    fecha_now_new.year = date.year
                    fecha_now_new.month = date.month
                    fecha_now_new.day = date.day

                    fechaExistente = 0

                } else {
                    fechaExistente = 1
                    if (fecha_now_new != date){
                        calendar.unMarkDate(
                            fecha_now_new.year,
                            fecha_now_new.month,
                            fecha_now_new.day
                        )
                    }

                    fecha_now_new.year = 1900
                    fecha_now_new.month = 1
                    fecha_now_new.day = 1
                }


                //fecha_now.set(date.year,date.month, date.day)
                //fecha_now_new.day = DateData(date.year,date.month,date.day)
                //fecha_now_new!!.setMonth(date.month)
                // fecha_now_new.year = date.year
                // fecha_now_new.month = date.month
                //fecha_now_new.day = date.day
                /* fecha_1.year = date.year
                    fecha_1.month = date.year
                    fecha_1.date = date.year*/

                // }

                //fecha_1.set(date.year,date.month, date.day)
                /*fecha_now.year = date.year
                fecha_now.month = date.month
                fecha_now.date = date.day*/


                spinner?.setSelection(0)

                fechaSelect = String.format("%d / %d / %d", date.day, date.month, date.year)
                adapter = MainMantenimientos_vehi(view.context)
                recyclerMantenimientos.layoutManager = LinearLayoutManager(activity)
                recyclerMantenimientos.adapter = adapter

                //var listrecyclemant = mutableListOf<mantenientosAutos>()
                //Limpio lista de mantenimientos
                listrecyclemant.clear()

                for (i in 0 until listMantenimientos.size) {
                    if (fechaSelect == listMantenimientos[i].fecha) {
                        listrecyclemant.add(listMantenimientos[i])
                    }
                }

                if (listrecyclemant.size !== 0) {
                    zonaSeleccion.visibility = View.VISIBLE
                    listarecyclemant = listrecyclemant
                    observeData()
                } else {
                    zonaSeleccion.visibility = View.INVISIBLE
                }


            }
        })

    }

    fun traervehiculos(){

        viewModel.traerDatosVehiculos(datosUsuario!!.uid).observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                listVehículos = it

                for (i in 0 until listVehículos.size) {
                    traerFechasMant(
                        listVehículos[i].id_auto,
                        listVehículos[i].aut_marca_c + " " + listVehículos[i].aut_modelo_c,
                        listVehículos[i].aut_patente_c
                    )
                }

                /*Cargo datos en desplegable de selección*/
                for (i in 0 until listVehículos.size) {
                    //itemList.add(listVehículos[i].aut_marca_c + " " +  listVehículos[i].aut_modelo_c + " " + listVehículos[i].aut_patente_c)
                    //itemList[0] = "Todos - " + listVehículos.size + " Vehículos"
                    txtCantVeh.text =
                        "Vehículos Disponibles: " + listVehículos.size.toString()
                    itemList[0] = "Todos"
                    itemList.add(listVehículos[i].aut_patente_c)
                }

                spinner = spinnes
                //arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, itemList)
                arrayAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    itemList
                )
                spinner?.adapter = arrayAdapter
                spinner?.onItemSelectedListener = this

            })

    }

    fun traerFechasMant(idAuto: String, desVeh: String, patente: String) {

        viewModel.traerFechasServicios(idAuto, desVeh, patente).observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {

                for (o in 0 until it.size) {
                    var mantenimientos_auto = mantenientosAutos(
                        it[o].fecha,
                        it[o].tipoMant,
                        it[o].desVehiculo,
                        it[o].patente,
                        it[o].kilometraje
                    )
                    listMantenimientos.add(mantenimientos_auto)
                }

                var calendarView = view?.findViewById(R.id.calendar) as MCalendarView
                //var calendarView = view?.findViewById(R.id.calendar) as MCalendarView

                for (i in 0 until listMantenimientos.size) {

                    var datos = listMantenimientos[i].fecha.split(" / ")

                    //var dates: ArrayList<DateData> = ArrayList()
                    var dates: DateData = (DateData(
                        datos[2].toInt(),
                        datos[1].toInt(),
                        datos[0].toInt()
                    ))

                    calendarView.markDate(
                        DateData(dates.year, dates.month, dates.day).setMarkStyle(
                            MarkStyle.BACKGROUND, Color.RED

                        )
                    )
                }
            })

        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = View.GONE
        calendar.visibility = View.VISIBLE

    }


    fun observeData(){
        adapter.setListData(listarecyclemant)
        adapter.notifyDataSetChanged()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var paten:String = parent?.getItemAtPosition(position) as String
        //Toast.makeText(context, "$item", Toast.LENGTH_SHORT).show()

        //******************************************************************************************
        if (position != 0){

            var listrecyclemante = mutableListOf<mantenientosAutos>()

            listrecyclemante.clear()
            for (i in 0 until listMantenimientos.size) {
                if (paten == listMantenimientos[i].patente && fechaSelect == listMantenimientos[i].fecha) {
                    listrecyclemante.add(listMantenimientos[i])
                }
            }

            //listarecyclemant.clear()
            listarecyclemant = listrecyclemante
            observeData()
        }else{

            if (listarecyclemant.size > 0){
                // listarecyclemant.clear()
                listarecyclemant = listrecyclemant
                observeData()
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Toast.makeText(context, "Mensaje", Toast.LENGTH_SHORT).show()
    }

    //Pop transmisión
    fun popTransmision(){
        val dialogBuilder = AlertDialog.Builder(activity)
        var patentes = arrayOfNulls<String>(listVehículos.size)

        for (i in 0 until listVehículos.size) {
            patentes.set(i, listVehículos[i].aut_patente_c)
        }

        dialogBuilder.setItems(patentes){ dialog, which ->
            var option = patentes[which]
            var vehiculo:Autos = Autos()

            if (option!= null || option != "") {

                for (i in 0 until listVehículos.size) {
                    if (listVehículos[i].aut_patente_c == option) {
                        vehiculo = listVehículos[i]
                    }
                }

                val bundle = Bundle()
                bundle.putParcelable("auto", vehiculo)
                findNavController().navigate(R.id.Mantenimientos,bundle)

            }

        }

        val alert = dialogBuilder.create()
        alert.setTitle("Elegir a que patente se le agrega el Mantenimiento:")
        alert.show()
    }





    }



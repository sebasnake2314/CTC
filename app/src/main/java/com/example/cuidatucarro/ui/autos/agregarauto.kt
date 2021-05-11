package com.example.cuidatucarro.ui.autos

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.contentValuesOf
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cuidatucarro.R
import com.example.cuidatucarro.data.network.RepoImplement
import com.example.cuidatucarro.domain.UseCaseImpl
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.viewmodel.AutoViewModel
import com.example.cuidatucarro.viewmodel.MainViewModel
import com.example.cuidatucarro.viewmodel.VMFactory
import com.example.cuidatucarro.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import io.grpc.Context
import kotlinx.android.synthetic.main.fragment_agregarauto.*
import kotlinx.android.synthetic.main.fragment_agregarauto.view.*
import kotlinx.android.synthetic.main.fragment_agregarauto.view.txtMarca
import kotlinx.android.synthetic.main.fragment_agregarauto.view.txtModelo
import kotlinx.android.synthetic.main.fragment_auto.*
import kotlinx.android.synthetic.main.item_row_autos.view.*
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.internal.artificialFrame
import kotlinx.coroutines.tasks.await
import java.util.*
import android.media.MediaPlayer.create as mediaPlayerCreate


class agregarauto : Fragment() {

    private val Auth = FirebaseAuth.getInstance().currentUser
    private val viewModel by lazy {ViewModelProvider(this).get(AutoViewModel::class.java) }
    private val transmision = arrayOf("Automático", "Sincrónica")
    private val selectpickture = arrayOf("Galería", "Cámara")

    private val viewModelauto by lazy {ViewModelProvider(this, VMFactory(UseCaseImpl(RepoImplement()))).get(MainViewModel::class.java)}

    private lateinit var txtPatente: EditText
    private lateinit var txtMarca: EditText
    private lateinit var txtModelo: EditText
    private lateinit var txtKm: EditText
    private lateinit var txtTrasmision: TextView
    private lateinit var auto:Autos
    private lateinit var actAutos:MutableList<Autos>
    private var foto: Uri? = null
    private val REQUEST_GALERIA = 1001
    private val REQUEST_CAMERA = 1002
    private var metodo:Int = 0

    var fotoCamarara: Uri? = null
    var mediaPlayer: MediaPlayer? = null
    var selectedPhotoUri: Uri? = null
    var urlimage:String = ""

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
        val fragment = inflater.inflate(R.layout.fragment_agregarauto, container, false)
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auto.aut_patente_c != "") {
            //Carga de datos de vehículo
            view.txtPatente.setText(auto.aut_patente_c)
            view.txtPatente.isEnabled = false
            view.txtMarca.setText(auto.aut_marca_c)
            view.txtModelo.setText(auto.aut_modelo_c)
            view.txtKilo.setText(auto.aut_kilometraje_i.toString())
            view.txtTransmision.text = auto.aut_tipo_trasmision_c

            Glide.with(this).load(auto.aut_image).into(view.btnSelectPickture)

            //btnAddVeh.isClickable = true
            view.btnAddVeh.text = "Actualizar"
            view.textView4.text = "Datos de Vehículos"
            view.textviewcambiofoto.text = "Presione para cambiar foto"

            metodo = 1
            foto = auto.auto_uri_foto.toString().toUri()
        }

        btnAddVeh.setOnClickListener {

            txtPatente = view.findViewById(R.id.txtPatente)
            txtPatente = view.findViewById(R.id.txtPatente)
            txtMarca = view.findViewById(R.id.txtMarca)
            txtModelo = view.findViewById(R.id.txtModelo)
            txtKm = view.findViewById(R.id.txtKilo)

            if (!TextUtils.isEmpty(txtPatente.text) && !TextUtils.isEmpty(txtMarca.text) && !TextUtils.isEmpty(txtModelo.text) && !TextUtils.isEmpty(txtKm.text) && !TextUtils.isEmpty(txtTransmision.text.toString()) && foto != null) {
                btnAddVeh.isClickable = false
                progressBarAddAuto.visibility = view.visibility
                btnAddVeh.text = "Cargando..."

                if (metodo == 0){
                    uploadImage(foto,"")
                }else{

                    actAutos = mutableListOf<Autos>()

                   // uploadImage(foto,auto.aut_uri_foto)

                    //val listData = mutableListOf<Autos>()

                    //val datoAutos = Autos(auto.id_auto,"", auto.aut_patente_c, txtMarca.text.toString() ,txtModelo.text.toString(),txtKm.text.toString().toLong(),txtTrasmision.text.toString())
                    val transmision = txtTransmision.text.toString()
                    val datoAutos = Autos(auto.id_auto!!,""!!, auto.aut_patente_c!!, txtMarca.text.toString()!! ,txtModelo.text.toString()!!,txtKm.text.toString().toLong()!!,transmision!!)


                    actAutos.add(datoAutos)

                    uploadImage(foto,auto.auto_uri_foto)

                }
                btnAddVeh.isClickable = true
            }else{

                if(TextUtils.isEmpty(txtPatente.text)){
                    txtPatente.setError("La Patente es un campo Obligatiorio para completar")
                }

                if(TextUtils.isEmpty(txtMarca.text)){
                    txtMarca.setError("La Marca es un campo Obligatiorio para completar")
                }

                if(TextUtils.isEmpty(txtModelo.text)){
                    txtModelo.setError("El Modelo es un campo Obligatiorio para completar")
                }

                if(TextUtils.isEmpty(txtKm.text)){
                    txtKm.setError("El Kilometraje es un campo Obligatiorio para completar")
                }

                if(TextUtils.isEmpty(txtTransmision.text.toString())){
                   /* Toast.makeText(activity, "La txtTransmision es un campo Obligatiorio para completar", Toast.LENGTH_SHORT).show()*/
                    txtTransmision.setError("La txtTransmision es un campo Obligatiorio para completar")
                }


               /* Toast.makeText(activity, "Completa todos los campos solicitados", Toast.LENGTH_SHORT).show()*/
            progressBarAddAuto.visibility = View.INVISIBLE
            btnAddVeh.isClickable = true
        }
        }
        txtTransmision.setOnClickListener{
            popTransmision()
       }
        btnSelectPickture.setOnClickListener{
            popSelectPickture()
        }
    }


    //Se comprueba si el usuario autorizó permisos a la aplicación
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_GALERIA ->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    muestraGaleria()
                else{
                    Toast.makeText(activity,"No se puede acceder a las imagenes", Toast.LENGTH_LONG).show()
                }
            }
            REQUEST_CAMERA ->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    muestraCamara()
                }else{
                    Toast.makeText(activity,"No se puede acceder a la cámara", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    //Permisos Cámara
    private fun seleccionCamara(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                ||ActivityCompat.checkSelfPermission(requireContext() ,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //Permiso denegado
                val permisoCamara = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisoCamara,REQUEST_CAMERA)
             }else{
                muestraCamara()
            }
        }else{
            muestraCamara()
        }
    }

    //Permisos Galería
    private fun selecionGaleria(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //Permiso denegado
                val permisoArchivos = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permisoArchivos,REQUEST_GALERIA)
            }else{
                muestraGaleria()
            }
        }else{
            muestraGaleria()
        }
    }

    //Se abre la Galería de fotos
    private fun muestraGaleria(){
        /*Toast.makeText(activity,"Aqui van las imagenes", Toast.LENGTH_LONG).show()*/
        val intentGaleria = Intent(Intent.ACTION_PICK)
        intentGaleria.type = "image/*"
        startActivityForResult(intentGaleria, REQUEST_GALERIA)
    }

    //Activar Camara
    private fun muestraCamara(){
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "Nueva Imagen")
        fotoCamarara = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fotoCamarara)
        startActivityForResult(camaraIntent,REQUEST_CAMERA)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALERIA){
            btnSelectPickture.setImageURI(data?.data)
            foto = data?.data!!
        }

        if(resultCode == Activity.RESULT_OK && requestCode ==REQUEST_CAMERA){
            btnSelectPickture.setImageURI(fotoCamarara)
            foto = fotoCamarara.toString().toUri()
        }

    }

    //Ventana de guardado con Éxito
    private fun showAlertDialogSuccess() {
        var dialogBuilder = AlertDialog.Builder(activity)
        val layoutView = layoutInflater.inflate(R.layout.activity_vehiculo_agregado, null)
        val dialogButton =
            layoutView.findViewById<Button>(R.id.btnOk)
        dialogBuilder.setView(layoutView)
        dialogBuilder.setCancelable(false)
        var alertDialog = dialogBuilder.create()
        alertDialog.show()
        mediaPlayer = mediaPlayerCreate(context, R.raw.alarma)
        mediaPlayer?.start()
        dialogButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    //Pop transmisión
    fun popTransmision(){
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setItems(transmision){dialog, which ->
            txtTransmision.setError(null)
            txtTransmision.setText(transmision[which])
        }
        val alert = dialogBuilder.create()
        alert.setTitle("Selecione la Transmisión")
        alert.show()
    }

    //pop seleción de imagen
    fun popSelectPickture(){
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setItems(selectpickture){dialog, which ->
            var option = selectpickture[which]

         if (option == "Galería"){
             selecionGaleria()
         }else{
             seleccionCamara()
         }

        }
        val alert = dialogBuilder.create()
        alert.setTitle("Selecciones una opción")
        alert.show()
    }

    //Retorna la Url de la Imagen selecionada al vehículo a agregar
    fun obtenerUrlImagen(image:Uri?){
    var url: String = ""
    viewModelauto.image = image
    viewModelauto.subirimagen.observe(viewLifecycleOwner,Observer { result ->
        when(result){
            is Resource.Loading->{
                viewModelauto.image = image
            }
            is Resource.Success->{
                 url = result.data
              /*  agregarAuto(url)*/
               /* Toast.makeText(activity,"Ocurrio un problema $url",Toast.LENGTH_SHORT).show()*/
            }
            is Resource.Failure->{
                  Toast.makeText(activity,"Ocurrio un problema ${result.exception.message}",Toast.LENGTH_SHORT).show()
            }
        }
    })

    }

    //Función Agregar Auto
    private fun agregarAuto(urlImage:String, uriPhoto:String){
        val patente = txtPatente.text.toString()
        val marca = txtMarca.text.toString()
        val model = txtModelo.text.toString()
        val km = txtKm.text.toString()
        val transmision = txtTransmision.text.toString()

            viewModel.agregarNuevoVehocilo(
                Auth?.uid.toString(),
                patente,
                marca,
                model,
                km.toLong(),
                transmision,
                urlImage,
                uriPhoto)
            showAlertDialogSuccess()

            btnAddVeh.isClickable = true
            findNavController().navigate(R.id.navigation_home)

    }

    //Función Atualizar Datos vehículo
    private fun actualizarAuto(urlImage:String, uriPhoto:String){
        val idAuto:String = actAutos[0].id_auto
        val marca = actAutos[0].aut_marca_c
        val model = actAutos[0].aut_modelo_c
        val km = actAutos[0].aut_kilometraje_i
        val transmision = actAutos[0].aut_tipo_trasmision_c

        viewModel.actualizarDatosVehiculos(
            idAuto,
            Auth?.uid.toString(),
            marca,
            model,
            km,
            transmision,
            urlImage,
            uriPhoto)
        btnAddVeh.isClickable = true
        findNavController().navigate(R.id.navigation_home)
        Toast.makeText(activity,"Actualización Realizada con Éxito",Toast.LENGTH_SHORT).show()
    }

    //Subir Imagen a Firestorage
    private fun uploadImage(selectedPhotoUri:Uri?, uriPhoto:String){
        if (selectedPhotoUri == null) return
        var filename:String = ""


        if(metodo == 1){
            filename = uriPhoto

            val  refe= FirebaseStorage.getInstance().getReference("/images/$filename")

            refe.putFile(selectedPhotoUri!!)

                refe.downloadUrl.addOnSuccessListener {
                    urlimage = it.toString()
                    actualizarAuto(urlimage, filename)
                }

        }else {

            filename = UUID.randomUUID().toString() + Auth?.uid.toString()
            val  ref= FirebaseStorage.getInstance().getReference("/images/$filename" )

            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        urlimage = it.toString()

                        if (metodo==0){
                            agregarAuto(urlimage, filename)
                        }

                    }
                }
        }

    }
}




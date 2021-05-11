package com.example.cuidatucarro.ui.Perfil

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cuidatucarro.R
import com.example.cuidatucarro.data.network.RepoImplement
import com.example.cuidatucarro.domain.UseCaseImpl
import com.example.cuidatucarro.ui.LoginActivity
import com.example.cuidatucarro.ui.MenuPrincipal
import com.example.cuidatucarro.viewmodel.MainViewModel
import com.example.cuidatucarro.viewmodel.VMFactory
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.example.cuidatucarro.vo.Resource
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.item_row_autos.view.*

class PerfilFragment : Fragment() {

    private val viewModel by lazy {ViewModelProvider(this,VMFactory(UseCaseImpl(RepoImplement()))).get(MainViewModel::class.java)}
    private val Auth = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_perfil, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtIdUsuario.text = Auth?.uid.toString()

        txtNombreUsuario.text = Auth?.displayName.toString()

        if ((Auth?.displayName.toString() == "") or (Auth?.displayName.toString() == "null"))  {
            observeData()
        }  else{
            txtNombreUsuario.text = Auth?.displayName.toString()
        }

        var image = Auth?.photoUrl.toString()

        if (image != "null"){
/*            Picasso.get().load(Auth?.photoUrl.toString()).into(imgPerfil)*/

            Glide.with(this).load(Auth?.photoUrl.toString()).into(imgPerfil2)
        }

        txtEmail.text = Auth?.email.toString()

        btnSalir.setOnClickListener{

            var dialogBuilder = android.app.AlertDialog.Builder(activity).create()
            dialogBuilder.setTitle("Cerrando Sessión")
            dialogBuilder.setMessage("¿Esta seguro en salir de su Sessión?")
            dialogBuilder.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "Salir"
            ) { _: DialogInterface?, _: Int ->
                /*finish()*/
                /*    MenuPrincipal().finish()
                LoginActivity().finish()*/
              /*  activity?.finish()*/
                val fAuth = FirebaseAuth.getInstance()
                fAuth.signOut()
                findNavController().navigate(R.id.loginActivity)
                activity?.finish()
            }
            dialogBuilder.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { _: DialogInterface?, _: Int ->
                dialogBuilder.cancel()
            }
            dialogBuilder.show()


        }

    }




    private fun showAlertDialogSuccess() {
        var dialogBuilder = android.app.AlertDialog.Builder(activity).create()
        dialogBuilder.setTitle("Saliendo de Cuida tu carro")
        dialogBuilder.setMessage("¿Esta seguro en salir de la aplicación?")
        dialogBuilder.setButton(
            AlertDialog.BUTTON_POSITIVE,
            "Salir"
        ) { _: DialogInterface?, _: Int ->
            /*finish()*/
            /*    MenuPrincipal().finish()
            LoginActivity().finish()*/
            activity?.finish()
        }
        dialogBuilder.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { _: DialogInterface?, _: Int ->
            dialogBuilder.cancel()
        }
        dialogBuilder.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

               activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
                   override fun handleOnBackPressed() {
                 /*      showAlertDialogSuccess()*/
                       activity?.finish()
                   }
               })
    }

    private fun observeData(){
        viewModel.idUsuario = Auth?.uid.toString()
        viewModel.fetchdatosusuario.observe(viewLifecycleOwner, Observer { result->

            when(result){

                is Resource.Loading->{
                progressBar.visibility = View.VISIBLE
                    viewModel.idUsuario = Auth?.uid.toString()
                }

                is Resource.Success->{
                    val NombreUsu = result.data
                    txtNombreUsuario.text = NombreUsu
                    progressBar.visibility = View.INVISIBLE
                }

                is Resource.Failure->{
                    progressBar.visibility = View.INVISIBLE
              /*  Toast.makeText(this,"Ocurrio un problema ${result.exception.message}",Toast.LENGTH_SHORT).show()*/
                }


            }
        })


    }


/*    private fun finishActivity() {
        if (activity != null) {
            finishActivity()
            requireActivity().finish()
        }
    }*/

}
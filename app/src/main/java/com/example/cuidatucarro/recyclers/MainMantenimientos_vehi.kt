package com.example.cuidatucarro.recyclers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cuidatucarro.R
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.objetos.mantenientosAutos
import kotlinx.android.synthetic.main.item_row_mantenimientos.view.*


class MainMantenimientos_vehi(private val context: Context): RecyclerView.Adapter<MainMantenimientos_vehi.MainViewHolder>() {

    private var dataList = mutableListOf<mantenientosAutos>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMantenimientos_vehi.MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_mantenimientos,parent,false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainMantenimientos_vehi.MainViewHolder, position: Int) {
        val mant = dataList[position]
        holder.bindView(mant, position)
    }

    fun setListData(data: MutableList<mantenientosAutos>){
        dataList = data
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    inner class MainViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(mante: mantenientosAutos, position: Int){
            itemView.txtpatente.text = mante.patente
            itemView.txtvehiculo.text = mante.desVehiculo
            itemView.txtServicio.text = mante.tipoMant
            itemView.txtKilometraje.text = mante.kilometraje.toString()
        }
    }



}
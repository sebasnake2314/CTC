package com.example.cuidatucarro.recyclers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cuidatucarro.R
import com.example.cuidatucarro.objetos.Autos
import com.example.cuidatucarro.objetos.mantenientosAutos
import com.example.cuidatucarro.objetos.tipoMatenimientoMotor
import kotlinx.android.synthetic.main.item_row_autos.view.*

class MainAdapter(private val context: Context,
                  private val itemClickListener:OnAutoClickListener
):RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var dataList = mutableListOf<Autos>()

    interface OnAutoClickListener{
        fun onImageClick(autImage: String)
        fun onItemClik(auto:Autos, position: Int)
    }

    fun setListData(data: MutableList<Autos>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_row_autos,parent,false)

        return MainViewHolder(view)
    }
    
    override fun getItemCount(): Int {
        return if (dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user, position)
    }

    inner class MainViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(user:Autos, position: Int){
            itemView.setOnClickListener{ itemClickListener.onItemClik(user,position)}
            itemView.circleImagenView.setOnClickListener{itemClickListener.onImageClick(user.aut_image)}
            //Glide.with(context).load(user.aut_image).into(itemView.circleImagenView)
            Glide.with(context).load(user.aut_image).centerCrop().into(itemView.circleImagenView)
            itemView.txtMarca.text = user.aut_marca_c
            itemView.txtModelo.text = user.aut_modelo_c
            itemView.txtKilometraje.text = user.aut_kilometraje_i.toString()
        }
    }

}
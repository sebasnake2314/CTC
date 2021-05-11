package com.example.cuidatucarro.objetos
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class mantenimientoMotor(
    val idVehiculo:String = "",
    val servicio:String = "",
    val fecha:String = "",
    val kilimetraje:Int = 0,
    val tipoAceite:String = "",
    val MarcaFiltro:String = "",
    val lugarMantenimiento:String =""
):Parcelable

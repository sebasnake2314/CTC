package com.example.cuidatucarro.objetos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Autos(
    val id_auto:String = "",
    val aut_image: String = "DEFAULT URL",
    val aut_patente_c: String = "",
    val aut_marca_c: String = "",
    val aut_modelo_c: String = "",
    val aut_kilometraje_i: Long = 0,
    val aut_tipo_trasmision_c: String = "",
    val auto_uri_foto:String = ""
):Parcelable
package com.example.cuidatucarro.objetos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class tipoMatenimientoMotor(
    val tipoMantenimiento: String = ""
):Parcelable

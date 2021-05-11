package com.example.cuidatucarro.ui.autos

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cuidatucarro.R
import kotlinx.android.synthetic.main.activity_imagen_vehiculo.*

class ImagenVehiculo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagen_vehiculo)
        if(intent.extras != null){
           Glide.with(this).load(intent.getStringExtra("imageURL")).into(photo_view)
        }
    }
}
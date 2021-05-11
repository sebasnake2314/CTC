package com.example.cuidatucarro.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cuidatucarro.R
import java.lang.Exception
import java.util.*

class SplashScrennActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screnn)


        val background = object : Thread(){
            override  fun run () {
                try {
                Thread.sleep(5000)

                    val intent = Intent(baseContext, LoginActivity::class.java)
                    startActivity(Intent())
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
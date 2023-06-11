package com.apm.petmate.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apm.petmate.R

class ProtectoraDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protectora_detail)

        var name = this.intent.extras?.getString("name")
        var imagen = this.intent.extras?.getString("imagen")

        println("NAME :" + name)
        println("IMAGEN :" + imagen)
    }
}
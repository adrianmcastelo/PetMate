package com.apm.petmate.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.apm.petmate.R

class StartApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_app)

        var register = findViewById<Button>(R.id.buttonRegister)
        var login = findViewById<Button>(R.id.buttonLogin)


        login.setOnClickListener{
            val intent = Intent(this, Login::class.java).apply {  }
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java).apply {  }
            startActivity(intent)
        }
    }
}
package com.apm.petmate.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.R
import org.json.JSONObject
import com.android.volley.Request
import com.apm.petmate.MainActivity
import com.apm.petmate.utils.VolleyApi

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        var userNamet = findViewById<EditText>(R.id.editTextLoginCorreo)
        var passwordt = findViewById<EditText>(R.id.editTextLoginContraseña)
        var btnSubmit = findViewById<Button>(R.id.buttonLoginViewLogin)

        btnSubmit.setOnClickListener {
            val user_name = userNamet.text.toString()
            val password = passwordt.text.toString()
            login(user_name, password)
            //Toast.makeText(this@Login, user_name, Toast.LENGTH_LONG).show()
        }
    }

    fun login(username: String, password: String) {

        val url = "http://10.0.2.2:8000/petmate/authenticate"

        val jsonO = JSONObject()
            .put("username", username)
            .put("password", password)

        println(jsonO.toString())

        val request = JsonObjectRequest(
            Request.Method.POST,url, jsonO,
            { response ->
                println("La respuesta es : $response")
                var token = JSONObject(response.toString()).getString("token")
                println("token del usuario: " + token)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("token", token)

                startActivity(intent)
            },
            { error -> error.printStackTrace() }
        )
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }
}
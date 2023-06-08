package com.apm.petmate.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.utils.VolleyApi
import org.json.JSONObject

class RegisterProtectora : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_protectora)

        var user = this.intent.extras?.getString("user")
        var pass = this.intent.extras?.getString("pass")

        var nombre = findViewById<EditText>(R.id.RegisterProtectoraNombre).text.toString()
        var dir = findViewById<EditText>(R.id.RegisterProtectoraDireccion).text.toString()
        var ubi = findViewById<EditText>(R.id.RegisterProtectoraUbicacion).text.toString()
        var tlf = findViewById<EditText>(R.id.RegisterProtectoraTelefono).text.toString()
        var url = findViewById<EditText>(R.id.RegisterProtectoraURL).text.toString()
        var correo = findViewById<EditText>(R.id.RegisterProtectoraCorreo).text.toString()
        var descripcion = findViewById<EditText>(R.id.RegisterProtectoraDescripcion).text.toString()
        var btnReister = findViewById<Button>(R.id.RegisterProtectoraButton)

        btnReister.setOnClickListener{
            register(user!!, pass!!, nombre, dir, ubi, tlf, url, correo, descripcion)
        }
    }

    fun register(user: String, pass: String, nombre: String, dir: String, ubicacion: String,
                 telefono: String, urlP: String, correo: String, descripcion: String) {

        val url = "http://10.0.2.2:8000/petmate/protectora/register"

        val jsonO = JSONObject()
            .put("username", user)
            .put("pass", pass)
            .put("name", nombre)
            .put("direccion", dir)
            .put("ubicacion", ubicacion)
            .put("url", urlP)
            .put("correo", correo)
            .put("descripcion", descripcion)
            .put("telefono", telefono)

        println(jsonO.toString())

        val request = JsonObjectRequest(
            Request.Method.POST,url, jsonO,
            { response ->
                println("La respuesta es : $response")
                var token = JSONObject(response.toString()).getString("token")
                var id = JSONObject(response.toString()).getString("IdProtectora")

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("token", token)
                intent.putExtra("IdProtectora", id)
                startActivity(intent)
            },
            { error -> error.printStackTrace() }
        )
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }
}
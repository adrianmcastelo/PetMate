package com.apm.petmate.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.utils.VolleyApi
import org.json.JSONObject

class RegisterUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        var userNamet = findViewById<EditText>(R.id.editTextRegisterCorreo)
        var passwordt = findViewById<EditText>(R.id.editTextRegisterContraseña)
        var btnSubmit = findViewById<Button>(R.id.buttonRegisterViewLogin)
        var switch = findViewById<Switch>(R.id.SwitchProtectora)

        btnSubmit.setOnClickListener {
            val user_name = userNamet.text.toString()
            val password = passwordt.text.toString()
            if (switch.isChecked){
                val intent = Intent(this, RegisterProtectora::class.java)
                intent.putExtra("user", user_name)
                intent.putExtra("pass", password)
                startActivity(intent)
            }else {
                register(user_name, password)
            }
        }
    }

    fun register(username: String, password: String) {

        val url = "http://10.0.2.2:8000/petmate/user/register"

        val jsonO = JSONObject()
            .put("username", username)
            .put("pass", password)

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
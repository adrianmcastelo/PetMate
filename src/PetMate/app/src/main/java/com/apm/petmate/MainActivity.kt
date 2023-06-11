package com.apm.petmate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.databinding.ActivityMainBinding
import com.apm.petmate.utils.Protectora
import com.apm.petmate.utils.VolleyApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var id: String? = null
        get() = field

    var token:String? = null
        get() = field

    var isProtectora:Boolean? = null
        get() = field

    var protectorasList: ArrayList<Protectora> = ArrayList<Protectora>()
        get() = field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getProtectoras()

        this.token = this.intent.extras?.getString("token")
        this.id = this.intent.extras?.getString("id")
        this.isProtectora = this.intent.extras?.getBoolean("isProtectora")
        println(id + " mainactivity")
        println(token + " mainactivity")
        println(isProtectora.toString() + " mainactivity")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_map, R.id.navigation_animals, R.id.navigation_favs
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getProtectoras() {
        val url = "http://10.0.2.2:8000/petmate/protectora/search"

        val request = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val protectoras = parseProtectoras(response)
                this.protectorasList = protectoras
            },
            { error -> error.printStackTrace() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token " + token
                return headers
            }
        }
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }

    private fun parseProtectoras(response: JSONObject):ArrayList<Protectora> {
        var protectorasJSON = JSONObject(response.toString()).getJSONArray("data")
        var protectoras = ArrayList<Protectora>()

        for (i in 0 until protectorasJSON.length()) {
            var protectoraJSON: JSONObject = protectorasJSON.getJSONObject(i)
            var protectora: Protectora = Protectora()
            protectora.id = protectoraJSON.getInt("id")
            protectora.name = protectoraJSON.getString("name").toString()
            protectora.direccion = protectoraJSON.getString("direccion").toString()
            protectora.ubicacion = protectoraJSON.getString("ubicacion").toString()
            protectora.telefono = protectoraJSON.getString("telefono").toString()
            protectora.url = protectoraJSON.getString("url").toString()
            protectora.correo = protectoraJSON.getString("correo").toString()
            protectora.descripcion = protectoraJSON.getString("descripcion").toString()
            protectora.imagen = protectoraJSON.getString("imagen").toString()
            protectora.longitud = protectoraJSON.getDouble("longitud")
            protectora.latitud = protectoraJSON.getDouble("latitud")

            protectoras.add(protectora)
        }

        println("PRotec list:" + protectoras.size)
        this.protectorasList = protectorasList
        return protectoras
    }

}
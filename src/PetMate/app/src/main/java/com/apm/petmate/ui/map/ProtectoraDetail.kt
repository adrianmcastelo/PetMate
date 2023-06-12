package com.apm.petmate.ui.map

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.databinding.ActivityProtectoraDetailBinding
import com.apm.petmate.utils.Protectora
import com.apm.petmate.utils.VolleyApi
import org.json.JSONObject


class ProtectoraDetail : AppCompatActivity() {

    private lateinit var binding: ActivityProtectoraDetailBinding

    private var token: String? = null
    private var idProtectora: Int? = null
    private lateinit var protectora: Protectora
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProtectoraDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idProtectora = this.intent.extras?.getInt("idProtectora")
        token = this.intent.extras?.getString("token")

        println("ID :" + idProtectora)
        println("TOKEN :" + token)

        getProtectora()
    }

    private fun setProtectora(protectora: Protectora) {
        binding.protectoraImage.setImageBitmap(protectora.imagen)
        binding.protectoraName.text = protectora.name
        binding.protectoraDescription.text = protectora.descripcion
        binding.protectoraPhone.text = protectora.telefono
        binding.protectoraMail.text = protectora.correo
        val protectoraUrl = binding.protectoraWeb
        protectoraUrl.movementMethod = LinkMovementMethod.getInstance()
        binding.protectoraLocation.text = protectora.direccion + ", " + protectora.ubicacion
        binding.protectoraWeb.text = protectora.url
    }

    private fun getProtectora() {
        val url = "http://10.0.2.2:8000/petmate/protectora/" + idProtectora
        val request = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                protectora = parseProtectora(response)
                setProtectora(protectora)
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

    private fun parseProtectora(response: JSONObject): Protectora {
        var protectora: Protectora = Protectora()

        protectora.id = response.getInt("id")
        protectora.name = response.getString("name").toString()
        protectora.direccion = response.getString("direccion").toString()
        protectora.ubicacion = response.getString("ubicacion").toString()
        protectora.telefono = response.getString("telefono").toString()
        protectora.url = response.getString("url").toString()
        protectora.correo = response.getString("correo").toString()
        protectora.descripcion = response.getString("descripcion").toString()
        protectora.latitud = response.getDouble("latitud")
        protectora.longitud = response.getDouble("longitud")
        protectora.imagen = base64ToBitmap(response.getString("imagen").toString())

        return protectora
    }

    private fun base64ToBitmap(image: String): Bitmap? {
        val imageBytes = Base64.decode(image, 0)
        val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return imageBitmap
    }
}
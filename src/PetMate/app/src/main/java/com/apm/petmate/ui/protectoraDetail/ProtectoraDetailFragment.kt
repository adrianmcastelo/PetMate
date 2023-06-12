package com.apm.petmate.ui.protectoraDetail

import android.app.Activity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.databinding.FragmentAnimalsBinding
import com.apm.petmate.databinding.FragmentProtectoraDetailBinding
import com.apm.petmate.ui.map.ProtectoraDetail
import com.apm.petmate.utils.AnimalUtils
import com.apm.petmate.utils.Protectora
import com.apm.petmate.utils.VolleyApi
import org.json.JSONObject


class ProtectoraDetailFragment : Fragment() {

    private lateinit var binding: FragmentProtectoraDetailBinding

    private var token: String? = null
    private var idProtectora: Int? = null
    private lateinit var protectora: Protectora

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentProtectoraDetailBinding.inflate(layoutInflater)

        val root: View = binding.root

        if ((activity as? MainActivity)?.id == null) {
            this.idProtectora = (activity as? ProtectoraDetail)?.id
        } else {
            this.idProtectora = (activity as? MainActivity)?.id
        }

        if ((activity as? MainActivity)?.token == null) {
            this.token = (activity as? ProtectoraDetail)?.token
        } else {
            this.token = (activity as? MainActivity)?.token
        }

        println("ID :" + idProtectora)
        println("TOKEN :" + token)

        getProtectora()

        return root
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
        VolleyApi.getInstance(getActivity() as Activity).addToRequestQueue(request)
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
        protectora.imagen = AnimalUtils().base64ToBitmap(response.getString("imagen").toString())

        return protectora
    }


}
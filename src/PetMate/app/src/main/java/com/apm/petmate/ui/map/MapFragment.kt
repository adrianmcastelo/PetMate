package com.apm.petmate.ui.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.databinding.FragmentMapBinding
import com.apm.petmate.utils.Protectora
import com.apm.petmate.utils.VolleyApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.lang.reflect.Array
import java.util.Dictionary

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null

    private lateinit var map:GoogleMap

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map,container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googlemaps: GoogleMap) {
        map = googlemaps
        getProtectoras()
    }

    private fun createMarkers(protectoras: ArrayList<Protectora>) {
        for (protectora in protectoras) {
            val coordinates = LatLng(protectora.latitud, protectora.longitud)
            val marker = MarkerOptions().position(coordinates)
                .title(protectora.name)
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_storefront_24)).anchor(0.0f, 1.0f)

            map.setOnMapClickListener {
                /*val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("token", token)
                startActivity(intent)*/
            }
            map.addMarker(marker)
        }

        if (!protectoras.isEmpty()) {
            val latZoom = protectoras[0].latitud
            val logZoom = protectoras[0].longitud
            val coordinatesZoom = LatLng(latZoom, logZoom)
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordinatesZoom, 12f),
                2000, null
            )
        }
    }

    private fun getProtectoras() {
        val url = "http://10.0.2.2:8000/petmate/protectora/search"

        val request = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val protectoras = parseProtectoras(response)
                createMarkers(protectoras)
            },
            { error -> error.printStackTrace() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token 18f9f31ea2f21561ad085598409b936cddf8b892"
                return headers
            }
        }
        VolleyApi.getInstance(getActivity() as Activity).addToRequestQueue(request)

    }

    private fun parseProtectoras(response: JSONObject):ArrayList<Protectora> {
        var protectorasJSON = JSONObject(response.toString()).getJSONArray("data")
        var protectoras = ArrayList<Protectora>()

        for (i in 0 until protectorasJSON.length()) {
            var protectoraJSON: JSONObject = protectorasJSON.getJSONObject(i)
            var protectora: Protectora = Protectora()
            protectora.name = protectoraJSON.getString("name").toString()
            protectora.longitud = protectoraJSON.getDouble("longitud")
            protectora.latitud = protectoraJSON.getDouble("latitud")

            protectoras.add(protectora)
        }
        return protectoras
    }
}
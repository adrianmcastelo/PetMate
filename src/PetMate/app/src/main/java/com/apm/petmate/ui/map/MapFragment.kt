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
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.lang.reflect.Array
import java.util.Dictionary

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    private var _binding: FragmentMapBinding? = null

    private lateinit var map:GoogleMap
    private var protectorasList: ArrayList<Protectora> = ArrayList<Protectora>()
        get() = field

    private var token:String? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.protectorasList = (activity as? MainActivity)?.protectorasList!!
        var id = (activity as? MainActivity)?.id
        this.token = (activity as? MainActivity)?.token

        println("ID en MAPA:" + id)
        println("TOKEN en MAPA:" + token)

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
        map.setOnInfoWindowClickListener(this)
        createMarkers(this.protectorasList)
    }

    override fun onInfoWindowClick(marker: Marker) {
        for (protectora in protectorasList) {
            if (protectora.latitud == marker.position.latitude &&
                protectora.longitud == marker.position.longitude) {
                val intent = Intent(context, ProtectoraDetail::class.java)
                intent.putExtra("name", protectora.name)
                intent.putExtra("direccion", protectora.direccion)
                intent.putExtra("ubicacion", protectora.ubicacion)
                intent.putExtra("telefono", protectora.telefono)
                intent.putExtra("url", protectora.url)
                intent.putExtra("correo", protectora.correo)
                intent.putExtra("descripcion", protectora.descripcion)
                intent.putExtra("imagen", protectora.imagen)
                println("ID PROT: " + protectora.id)
                startActivity(intent)
            }
        }
    }

    private fun createMarkers(protectoras: ArrayList<Protectora>) {
        for (protectora in protectoras) {
            val coordinates = LatLng(protectora.latitud, protectora.longitud)
            val marker = MarkerOptions().position(coordinates)
                .title(protectora.name)

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

}
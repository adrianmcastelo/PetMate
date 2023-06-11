package com.apm.petmate.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class RegisterProtectoraMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map:GoogleMap

    private var marker: Marker? = null
        get() = field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_protectora_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.ButtonMapSelect).setOnClickListener {
            if (marker != null) {
                (RegisterProtectora).marker = marker!!
            }
            finish()
        }
    }

    override fun onMapReady(googlemaps: GoogleMap) {
        map = googlemaps

        map.setOnMapClickListener(object: GoogleMap.OnMapClickListener {
            override fun onMapClick(latlng: LatLng) {
                if (marker == null){
                    val location = LatLng(latlng.latitude,latlng.longitude)
                    val newMarker = MarkerOptions().position(location).draggable(true)
                    marker = map.addMarker(newMarker)
                }else {
                    map.clear()
                    val location = LatLng(latlng.latitude,latlng.longitude)
                    val newMarker = MarkerOptions().position(location).draggable(true)
                    marker = map.addMarker(newMarker)
                }
            }
        })
        val coordinatesZoom = LatLng(40.684716, -3.891399)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinatesZoom, 6f),
            2000, null
        )
    }
}
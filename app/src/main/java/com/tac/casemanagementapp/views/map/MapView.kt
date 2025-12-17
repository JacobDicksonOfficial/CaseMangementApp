package com.tac.casemanagementapp.views.map

import android.app.Activity
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tac.casemanagementapp.R
import java.util.Locale

/**
 * Simple map screen used to select a location.
 */
class MapView : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment)
                    as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val defaultLoc = LatLng(53.3498, -6.2603) // Dublin
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 10f))

        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Selected location"))

            val address = getAddress(latLng)

            map.setOnMarkerClickListener {
                val result = intent
                result.putExtra("latitude", latLng.latitude)
                result.putExtra("longitude", latLng.longitude)
                result.putExtra("address", address)
                setResult(Activity.RESULT_OK, result)
                finish()
                true
            }
        }
    }

    // Converts coordinates into a readable address
    private fun getAddress(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val results = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
            results?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
        } catch (e: Exception) {
            "Unknown location"
        }
    }
}

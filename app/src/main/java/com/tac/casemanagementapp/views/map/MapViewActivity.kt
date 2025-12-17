package com.tac.casemanagementapp.views.map

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tac.casemanagementapp.R
import timber.log.Timber
import java.util.Locale

/**
 * Map screen where user selects and confirms a location.
 */
class MapViewActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private var currentMarker: Marker? = null

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

        map.setOnMapClickListener(this)
        map.setOnMarkerClickListener(this)

        // Default camera position (Dublin)
        val startLocation = LatLng(53.3498, -6.2603)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12f))
    }

    // User taps the map and places a marker
    override fun onMapClick(latLng: LatLng) {
        currentMarker?.remove()

        currentMarker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Selected location")
                .snippet("Tap marker to confirm")
        )

        map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    // User taps marker and confirms the location
    override fun onMarkerClick(marker: Marker): Boolean {
        val latLng = marker.position
        val address = getAddress(latLng)

        Timber.i("Location selected: $latLng | $address")

        val resultIntent = Intent().apply {
            putExtra("latitude", latLng.latitude)
            putExtra("longitude", latLng.longitude)
            putExtra("address", address)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()

        return true
    }

    // Converts coordinates to readable address
    private fun getAddress(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                listOfNotNull(
                    address.thoroughfare,
                    address.locality,
                    address.countryName
                ).joinToString(", ")
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            Timber.e(e, "Geocoding failed")
            "Unknown location"
        }
    }
}

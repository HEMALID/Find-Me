package com.example.findme.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.findme.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.findme.databinding.ActivityMapsBinding
import com.example.findme.model.HappyPlaceModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var mHappyPlaceDetails: HappyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            mHappyPlaceDetails =
                intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }

        if (mHappyPlaceDetails != null) {

            setSupportActionBar(binding.toolbarMap)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mHappyPlaceDetails!!.title

            binding.toolbarMap.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {

        val position = LatLng(
            mHappyPlaceDetails!!.latitude,
            mHappyPlaceDetails!!.longitude
        )
        googleMap.addMarker(MarkerOptions().position(position).title(mHappyPlaceDetails!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)

        /*mMap = googleMap

        val seoul = LatLng(37.5665, 126.9780)
        mMap.addMarker(MarkerOptions().position(seoul).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul))*/
    }
}
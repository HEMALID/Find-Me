package com.example.findme.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.findme.databinding.ActivityHappyPlaceDetailBinding
import com.example.findme.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_happy_place_detail.*

class HappyPlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHappyPlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var happyPlaceModel:HappyPlaceModel ?= null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceModel = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }

        if (happyPlaceModel != null){
            setSupportActionBar(toolbar_happy_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceModel.title

            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            binding.ivPlaceImage.setImageURI(Uri.parse(happyPlaceModel.image))
            binding.tvDescription.text = happyPlaceModel.description
            binding.tvLocation.text = happyPlaceModel.location

            btn_view_on_map.setOnClickListener {
                var i = Intent(this,MapsActivity::class.java)
                i.putExtra(MainActivity.EXTRA_PLACE_DETAILS,happyPlaceModel)
                startActivity(i)

            }
        }

    }
}
package com.example.findme.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findme.R
import com.example.findme.adapter.MyAdapter
import com.example.findme.database.DatabaseHandler
import com.example.findme.databinding.ActivityMainBinding
import com.example.findme.model.HappyPlaceModel
import com.example.findme.utils.SwipeToDeleteCallback
import com.example.findme.utils.SwipeToEditCallback
import com.google.android.material.behavior.SwipeDismissBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    companion object {
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE=1
        internal const val EXTRA_PLACE_DETAILS="extra_place_details"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        window.navigationBarColor=getColor(R.color.darkBlue)

        mainBinding.btnFloat.setOnClickListener {
            var i=Intent(applicationContext, AddHappyPlace::class.java)
            startActivityForResult(i, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlacesListFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesListFromLocalDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    private fun getHappyPlacesListFromLocalDB() {

        val dbHandler=DatabaseHandler(this)
        val getHappyPlacesList=dbHandler.getHappyPlacesList()

        if (getHappyPlacesList.size > 0) {
            mainBinding.recyclerView.visibility=View.VISIBLE
            mainBinding.noRecord.visibility=View.GONE
            setupHappyPlacesRecyclerView(getHappyPlacesList)
        } else {
            mainBinding.recyclerView.visibility=View.GONE
            mainBinding.noRecord.visibility=View.VISIBLE
        }
    }

    private fun setupHappyPlacesRecyclerView(happyPlacesList: ArrayList<HappyPlaceModel>) {

        mainBinding.recyclerView.layoutManager=LinearLayoutManager(this)
        mainBinding.recyclerView.setHasFixedSize(true)

        val placesAdapter=MyAdapter(this, happyPlacesList)
        mainBinding.recyclerView.adapter=placesAdapter

        placesAdapter.setOnClickListener(object : MyAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val i=Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                i.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(i)
            }
        })

        // FOR EDITING
        val editSwipeHandler=object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter=mainBinding.recyclerView.adapter as MyAdapter
                adapter.notifyEditItem(
                    this@MainActivity, viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper=ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(mainBinding.recyclerView)


        // FOR DELETE
        val deleteSwipeHandler=object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter=mainBinding.recyclerView.adapter as MyAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                getHappyPlacesListFromLocalDB()

            }
        }
        val deleteItemTouchHelper=ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(mainBinding.recyclerView)
    }
}
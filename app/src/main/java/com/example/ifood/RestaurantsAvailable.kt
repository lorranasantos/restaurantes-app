package com.example.ifood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RestaurantsAvailable : AppCompatActivity() {

    lateinit var rvRestaurants: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants_available)

        supportActionBar?.hide();

        rvRestaurants = findViewById(R.id.rv_rest_list)
        rvRestaurants.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}
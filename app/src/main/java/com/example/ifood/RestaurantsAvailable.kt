package com.example.ifood

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifood.adapter.RestaurantListAdapter
import com.example.ifood.api.CallRestaurants
import com.example.ifood.api.RetrofitApi
import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantsAvailable : AppCompatActivity() {

    lateinit var rvRestaurants: RecyclerView
    lateinit var restaurantList: RestaurantListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants_available)

        supportActionBar?.hide()

        rvRestaurants = findViewById(R.id.rv_rest_list)
        rvRestaurants.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        restaurantList = RestaurantListAdapter(this)

        rvRestaurants.adapter = restaurantList

        loadRestaurants()
    }

    private fun loadRestaurants() {
        val retrofit = RetrofitApi.getRetrofit()
        val receiveRestaurants = retrofit.create(CallRestaurants::class.java)
        val call = receiveRestaurants.getRestaurants()

        call.enqueue(object : Callback<List<Restaurants>> {
            override fun onFailure(call: Call<List<Restaurants>>, t: Throwable) {
                Toast.makeText(this@RestaurantsAvailable, "Ops! Acho que ocorreu um problema.", Toast.LENGTH_SHORT).show()
                Log.e("Erro_CONEXÃO", t.message.toString())
            }

            override fun onResponse(call: Call<List<Restaurants>>, response: Response<List<Restaurants>>) {
                val listRestaurants = response.body()
                if (listRestaurants != null) {
                    restaurantList.updateRestaurantList(listRestaurants)
                }
            }
        })
    }
}
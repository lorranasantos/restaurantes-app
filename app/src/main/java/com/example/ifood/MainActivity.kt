package com.example.ifood

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttonRegisterRest: CardView
    private lateinit var buttonListRest: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide();

        buttonRegisterRest = findViewById(R.id.go_register_restaurant)
        buttonRegisterRest.setOnClickListener(this)

        buttonListRest = findViewById(R.id.go_list_restaurants)
        buttonListRest.setOnClickListener(this)
    }

   override fun onClick(v: View){
        when(v.id){
            R.id.go_register_restaurant -> {

                val openRegisterRestaurant = Intent(this, RegisterAddressActivity::class.java)
                startActivity(openRegisterRestaurant)
            }
            R.id.go_list_restaurants -> {
                val openRestaurantsList = Intent(this, RestaurantsAvailable::class.java)
                startActivity(openRestaurantsList)
            }
        }
   }
}
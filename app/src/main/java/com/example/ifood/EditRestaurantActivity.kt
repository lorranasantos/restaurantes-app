package com.example.ifood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ifood.api.CallRestaurantCategory
import com.example.ifood.api.CallRestaurants
import com.example.ifood.api.RetrofitApi
import com.example.ifood.model.Categories
import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditRestaurantActivity : AppCompatActivity() {

    private lateinit var editRestName: EditText
    private lateinit var editRestDescription: EditText

    private lateinit var restaurantData: Restaurants

    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_restaurant)

        supportActionBar?.hide()

        val receivedId: Int = intent.getIntExtra("RestaurantId", 0)

        if (receivedId == 0) {
            Toast.makeText(this, "Invalid restaurant ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        editRestName = findViewById(R.id.restaurant_name_edit)
        editRestDescription = findViewById(R.id.restaurant_description_edit)
        saveButton = findViewById(R.id.button_save_edition)

        fetchRestaurant(receivedId)

        saveButton.setOnClickListener {
            saveRestaurantData(receivedId)
        }
    }

    private fun fetchRestaurant(id: Int) {
        val retrofit = RetrofitApi.getRetrofit()
        val restCall = retrofit.create(CallRestaurants::class.java)

        val call = restCall.getRestaurantId(id)
        call.enqueue(object : Callback<Restaurants> {
            override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) {
                if (response.isSuccessful) {
                    restaurantData = response.body() ?: Restaurants()
                    editRestName.setText(restaurantData.name)
                    editRestDescription.setText(restaurantData.description)
                } else {
                    Toast.makeText(this@EditRestaurantActivity, "Failed to fetch restaurant data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Restaurants>, t: Throwable) {
                Toast.makeText(this@EditRestaurantActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveRestaurantData(id: Int) {
        val retrofit = RetrofitApi.getRetrofit()
        val restCall = retrofit.create(CallRestaurants::class.java)

        val updatedName = editRestName.text.toString()
        val updatedDescription = editRestDescription.text.toString()

        val updatedRestaurant = Restaurants(
            id = id,
            name = updatedName,
            description = updatedDescription,
            category_id = restaurantData.category_id,
            image = restaurantData.image,
            address_id = restaurantData.address_id
        )

        val call = restCall.updateRestaurant(id, updatedRestaurant)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditRestaurantActivity, "Restaurant updated successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@EditRestaurantActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    Toast.makeText(this@EditRestaurantActivity, "Failed to update restaurant", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EditRestaurantActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
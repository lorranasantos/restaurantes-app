package com.example.ifood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.ifood.api.CallRestaurantCategory
import com.example.ifood.api.CallRestaurants
import com.example.ifood.api.RetrofitApi
import com.example.ifood.model.Categories
import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityRegisterRestaurant : AppCompatActivity() {

    private lateinit var buttonGoRegisterAddress: Button
    private lateinit var restaurantName: EditText
    private lateinit var restaurantDescription: EditText

    private lateinit var categoriesList: Spinner

    private lateinit var categoryMap: MutableMap<String, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_restaurant)

        supportActionBar?.hide();

        categoryMap = mutableMapOf()

        restaurantName = findViewById(R.id.restaurant_name_register)
        restaurantDescription = findViewById(R.id.restaurant_description_register)

        buttonGoRegisterAddress = findViewById(R.id.button_next_register)
        buttonGoRegisterAddress.setOnClickListener{
            saveRestaurantData()
        }

        categoriesList = findViewById(R.id.rest_category_list)

        fetchCategories()

    }

    private fun fetchCategories() {
        val retrofit = RetrofitApi.getRetrofit()
        val categoryCall = retrofit.create(CallRestaurantCategory::class.java)
        val categories = categoryCall.getCategories()

        categories.enqueue(object : Callback<List<Categories>> {
            override fun onResponse(call: Call<List<Categories>>, response: Response<List<Categories>>) {

                if (response.isSuccessful) {
                    response.body()?.let { categories ->

                        val categoryNames = categories.map { it.category }
                        val adapter = ArrayAdapter(this@ActivityRegisterRestaurant, android.R.layout.simple_spinner_item, categoryNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        categoriesList.adapter = adapter

                        for (category in categories) {
                            categoryMap[category.category] = category.id
                        }

                    }
                } else{
                    Log.e("API_CALL", "Failed to fetch categories: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Categories>>, t: Throwable) {
                Log.e("API_CALL", "Failed to fetch categories", t)
            }
        })
    }

    private fun saveRestaurantData() {
        val retrofit = RetrofitApi.getRetrofit()
        val restCall = retrofit.create(CallRestaurants::class.java)

        val selectedCategory = categoriesList.selectedItem.toString()

        val categoryId = categoryMap[selectedCategory] ?: 0

        val registerRestaurant = Restaurants(
            name = restaurantName.text.toString(),
            description = restaurantDescription.text.toString(),
            category_id = categoryId,
            image = "",
            address_id = 1
        )

        val call = restCall.createRestaurant(registerRestaurant)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("ta na api", "sss")
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ActivityRegisterRestaurant,
                        "Restaurant created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@ActivityRegisterRestaurant,
                        "Failed to create restaurant",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@ActivityRegisterRestaurant,
                    "Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
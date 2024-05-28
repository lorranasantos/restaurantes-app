package com.example.ifood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.ifood.api.CallRestaurantCategory
import com.example.ifood.api.CallRestaurants
import com.example.ifood.api.RetrofitApi
import com.example.ifood.model.Categories
import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantInfoActivity : AppCompatActivity() {

    private lateinit var restaurantName: TextView
    private lateinit var restaurantCategory: TextView
    private lateinit var restaurantDescription: TextView

    private  lateinit var deleteRestaurant: ImageView
    private lateinit var editRestaurant: ImageView

    private lateinit var restStreet: TextView
    private lateinit var restCity: TextView
    private lateinit var restState: TextView
    private lateinit var restCEP: TextView
    private lateinit var restNeighbourhood: TextView
    private lateinit var restComplement: TextView
    private lateinit var restNumber: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_info)

        restaurantName = findViewById(R.id.restaurant_name_get)
        restaurantCategory = findViewById(R.id.get_category)
        restaurantDescription = findViewById(R.id.get_description)

        deleteRestaurant = findViewById(R.id.delete_rest)
        editRestaurant = findViewById(R.id.edit_rest)

        supportActionBar?.hide();


        deleteRestaurant.setOnClickListener{
            deleteRestaurant()
        }
        editRestaurant.setOnClickListener{
            editRestaurant()
        }
        loadData()
    }
    private fun loadData(){
        val receivedId: Int = intent.getIntExtra("RestaurantId", 0)

        val retrofit = RetrofitApi.getRetrofit()
        val receiveRestaurants = retrofit.create(CallRestaurants::class.java)
        val call = receiveRestaurants.getRestaurantId(receivedId)

        call.enqueue(object : Callback<Restaurants> {
            override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) {
                val restaurant = response.body()
                if (restaurant != null) {
                    val categoryId = restaurant.category_id

                    val categoryCall = retrofit.create(CallRestaurantCategory::class.java)
                    val categoryCalled = categoryCall.getCategoryId(categoryId)

                    categoryCalled.enqueue(object : Callback<Categories> {
                        override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                            val category = response.body()
                            if (category != null ) {
                                restaurantCategory.text = category.category
                            } else {
                                restaurantCategory.text = "Unknown Category"
                            }
                        }

                        override fun onFailure(call: Call<Categories>, t: Throwable) {
                            Log.e("RestaurantInfoActivity", "Error fetching category data", t)
                            restaurantCategory.text = "Unknown Category"
                        }
                    })
                    restaurantName.text = restaurant.name
                    restaurantDescription.text = restaurant.description
                } else {
                    Toast.makeText(this@RestaurantInfoActivity, "No data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Restaurants>, t: Throwable) {
                Log.e("RestaurantInfoActivity", "Error fetching data", t)
                Toast.makeText(this@RestaurantInfoActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun deleteRestaurant() {
        val receivedId: Int = intent.getIntExtra("RestaurantId", 0)

        if (receivedId == 0) {
            Toast.makeText(this, "Invalid restaurant ID", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = RetrofitApi.getRetrofit()
        val receiveRestaurants = retrofit.create(CallRestaurants::class.java)
        val call = receiveRestaurants.deleteRestaurantId(receivedId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RestaurantInfoActivity, "Restaurant deleted successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RestaurantInfoActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("deleteRest", receivedId.toString())
                    Toast.makeText(this@RestaurantInfoActivity, "Failed to delete restaurant", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RestaurantInfoActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editRestaurant() {
        val receivedId: Int = intent.getIntExtra("RestaurantId", 0)

        if (receivedId == 0) {
            Toast.makeText(this, "Invalid restaurant ID", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this@RestaurantInfoActivity, EditRestaurantActivity::class.java)
        intent.putExtra("RestaurantId", receivedId)
        startActivity(intent)
    }
}
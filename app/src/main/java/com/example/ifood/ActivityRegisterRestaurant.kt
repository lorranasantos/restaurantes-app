package com.example.ifood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.ifood.api.CallRestaurantCategory
import com.example.ifood.api.RetrofitApi
import com.example.ifood.model.Categories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityRegisterRestaurant : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttonGoRegisterAddress: Button

    private lateinit var categoriesList: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_restaurant)

        val retrofit = RetrofitApi.getRetrofit()

        supportActionBar?.hide();

        buttonGoRegisterAddress = findViewById(R.id.button_next_register)
        buttonGoRegisterAddress.setOnClickListener(this)

        categoriesList = findViewById(R.id.rest_category_list)

        fetchCategories()

    }

    override fun onClick(v: View){
        when(v.id){
            R.id.button_next_register-> {

                val openRegisterAddress = Intent(this, RegisterAddressActivity::class.java)
                startActivity(openRegisterAddress)
            }
        }
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

                        categoryNames.forEach { name ->
                            println("Category: $name")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Categories>>, t: Throwable) {
                // Trate a falha aqui
            }
        })
    }
}
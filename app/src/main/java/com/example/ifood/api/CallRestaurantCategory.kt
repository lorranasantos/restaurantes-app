package com.example.ifood.api


import com.example.ifood.model.Categories
import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface CallRestaurantCategory {
    @GET("category")
    fun getCategories(): Call<List<Categories>>

    @GET("category/{id}")
    fun getCategoryId(@Path("id") id: Int): Call<Categories>


}
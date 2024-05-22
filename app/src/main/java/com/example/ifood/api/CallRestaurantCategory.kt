package com.example.ifood.api


import com.example.ifood.model.Categories
import retrofit2.Call
import retrofit2.http.GET

interface CallRestaurantCategory {

    @GET("category")
    fun getCategories(): Call<List<Categories>>
}
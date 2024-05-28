package com.example.ifood.api

import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface CallRestaurants {
    @GET("restaurant")
    fun getRestaurants(): Call<List<Restaurants>>

    @GET("restaurant/search?")
    fun searchRestaurants(@Query("name") name: String): Call<List<Restaurants>>

    @GET("restaurant/{id}")
    fun getRestaurantId(@Path("id") id: Int): Call<Restaurants>

    @POST("restaurant")
    fun createRestaurant(@Body restaurant: Restaurants): Call<Void>

    @DELETE("restaurant/{id}")
    fun deleteRestaurantId(@Path("id") id: Int): Call<Void>

    @PUT("restaurant/{id}")
    fun updateRestaurant(@Path("id") id: Int, @Body restaurant: Restaurants): Call<Void>
}
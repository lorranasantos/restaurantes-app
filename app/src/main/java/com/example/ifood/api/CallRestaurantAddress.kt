package com.example.ifood.api


import com.example.ifood.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CallRestaurantAddress {
    @GET("address")
    fun getAddress(): Call<List<Address>>

    @GET("address/{id}")
    fun getAddressId(@Path("id") id: Int): Call<Address>


}
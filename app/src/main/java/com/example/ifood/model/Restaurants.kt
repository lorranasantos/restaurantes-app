package com.example.ifood.model

data class Restaurants (
        var id: Int = 0,
        var name: String = "",
        var image: String? = "",
        var description: String = "",
        var category_id: Int = 0,
        var address_id: Int = 0
)
package com.example.ifood.model

data class Address(
    var id: Int = 0,
    var address: String = "",
    var house_number: Int = 0,
    var zip_code: Int = 0,
    var neighborhood: String = "",
    var city: String = "",
    var state: String = "",
    var complement:  String = "",

)
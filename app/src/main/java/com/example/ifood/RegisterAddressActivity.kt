package com.example.ifood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_address)

        supportActionBar?.hide();
    }
}
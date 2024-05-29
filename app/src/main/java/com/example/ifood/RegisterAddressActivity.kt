package com.example.ifood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.text.TextWatcher
import android.widget.Toast
import org.json.JSONObject
import android.text.Editable
import com.example.ifood.api.CallRestaurantAddress
import com.example.ifood.api.CallRestaurants
import com.example.ifood.api.RetrofitApi
import com.example.ifood.model.Address
import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.concurrent.Executors

class RegisterAddressActivity : AppCompatActivity() {

    private lateinit var cep: EditText
    private lateinit var city: EditText
    private lateinit var state: EditText
    private lateinit var address: EditText
    private lateinit var neighbourhood: EditText
    private lateinit var number: EditText
    private lateinit var complement: EditText
    private lateinit var save: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_address)

        supportActionBar?.hide();

        cep = findViewById(R.id.text_cep)
        city = findViewById(R.id.text_register_city)
        state = findViewById(R.id.text_register_state)
        address = findViewById(R.id.text_register_street)
        neighbourhood = findViewById(R.id.text_register_neighbourhood)
        number = findViewById(R.id.text_register_number)
        complement = findViewById(R.id.text_register_complement)
        save = findViewById(R.id.button_register_rest)

        cep.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 8) {
                    searchCEP()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        save.setOnClickListener{
            saveAddressData()

            val openRegisterRestaurant = Intent(this@RegisterAddressActivity, ActivityRegisterRestaurant::class.java)
            startActivity(openRegisterRestaurant)
        }

    }

    private fun searchCEP() {
        val cepText = cep.text.toString()

        val url = "https://viacep.com.br/ws/$cepText/json"

        if (cepText.isEmpty()) {
            Toast.makeText(this@RegisterAddressActivity, "Ops! Você não inseriu nenhum cep.", Toast.LENGTH_LONG).show()
        } else {
            Executors.newSingleThreadExecutor().execute {
                try {
                    val response = URL(url).readText()
                    println(response)

                    if (response.contains("\"erro\"")) { // Verifica se a resposta contém "erro"
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterAddressActivity,
                                "CEP INVÁLIDO! Tente novamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val jsonObject = JSONObject(response)

                        runOnUiThread {
                            address.setText(jsonObject.getString("logradouro"))
                            neighbourhood.setText(jsonObject.getString("bairro"))
                            city.setText(jsonObject.getString("localidade"))
                            state.setText(jsonObject.getString("uf"))
                        }
                    }
                } catch (p: Exception) {
                    p.printStackTrace()
                    Log.e("RegisterAddressActivity", "Erro ao consultar CEP", p)
                    runOnUiThread {
                        Toast.makeText(this@RegisterAddressActivity, "CEP INVÁLIDO! Tente novamente", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveAddressData() {
        val retrofit = RetrofitApi.getRetrofit()
        val addressCall = retrofit.create(CallRestaurantAddress::class.java)

        try {
            val addressText = address.text.toString()
            val houseNumber = number.text.toString().toInt()
            val zipCode = cep.text.toString().toInt()
            val neighbourhoodText = neighbourhood.text.toString()
            val cityText = city.text.toString()
            val stateText = state.text.toString()
            val complementText = complement.text.toString()

            // Log the values before creating the address
            Log.d("RegisterAddressActivity", "address: $addressText")
            Log.d("RegisterAddressActivity", "house_number: $houseNumber")
            Log.d("RegisterAddressActivity", "zip_code: $zipCode")
            Log.d("RegisterAddressActivity", "neighborhood: $neighbourhoodText")
            Log.d("RegisterAddressActivity", "city: $cityText")
            Log.d("RegisterAddressActivity", "state: $stateText")
            Log.d("RegisterAddressActivity", "complement: $complementText")


            val registerAddress = Address(
                address = addressText,
                house_number = houseNumber,
                zip_code = zipCode,
                neighborhood = neighbourhoodText,
                city = cityText,
                state = stateText,
                complement = complementText
            )

            val call = addressCall.createAddress(registerAddress)
            Log.e("endereco", registerAddress.address)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@RegisterAddressActivity,
                            "Endereço criado com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterAddressActivity,
                            "Falha ao criar endereço: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterAddressActivity,
                        "Erro ao criar endereço: " + t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } catch (e: NumberFormatException) {
            Toast.makeText(this@RegisterAddressActivity, "Por favor, insira valores numéricos válidos para CEP e número.", Toast.LENGTH_SHORT).show()
        }
    }
}
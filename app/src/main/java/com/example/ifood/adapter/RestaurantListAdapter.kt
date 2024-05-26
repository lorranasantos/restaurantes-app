package com.example.ifood.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ifood.R
import com.example.ifood.RestaurantInfoActivity
import com.example.ifood.RestaurantsAvailable
import com.example.ifood.api.CallRestaurantCategory
import com.example.ifood.api.RetrofitApi
import com.example.ifood.model.Categories
import com.example.ifood.model.Restaurants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantListAdapter(val context: Context) : RecyclerView.Adapter<RestaurantListAdapter.Holder>()  {

    var listItems = emptyList<Restaurants>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.holder_restaurant_list, parent, false)

        return Holder(view)
    }

    fun updateRestaurantList(restList: List<Restaurants>){
        listItems = restList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val restaurantItems = listItems[position]

        holder.name.text = restaurantItems.name
        loadCategoryName(holder.category, restaurantItems.category_id) // tentando pegar a categoria

        holder.cardRestaurantList.setOnClickListener{
            val intent = Intent(context, RestaurantInfoActivity::class.java)
            intent.putExtra("RestaurantId", restaurantItems.id)

            context.startActivity(intent)
        }
    }

    private fun loadCategoryName(textView: TextView, categoryId: Int) {
        val retrofit = RetrofitApi.getRetrofit()
        val callRestaurantCategory = retrofit.create(CallRestaurantCategory::class.java)
        val call = callRestaurantCategory.getCategoryId(categoryId)

        call.enqueue(object : Callback<Categories> {
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                val category = response.body()
                if (category != null) {
                    textView.text = category.category
                }
            }

            override fun onFailure(call: Call<Categories>, t: Throwable) {
                Log.e("Erro_CONEXÃO", t.message.toString())
                textView.text = "Unknown"
            }
        })
    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.text_view_rest_name)
        val category = view.findViewById<TextView>(R.id.tv_rest_category)
        val cardRestaurantList = view.findViewById<CardView>(R.id.card_restaurant_list)
    }
}
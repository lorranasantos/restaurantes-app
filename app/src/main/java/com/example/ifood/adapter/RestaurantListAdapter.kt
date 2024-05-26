package com.example.ifood.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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


    private fun loadCategoryName(textView: TextView, categoryId: Int) {
        val retrofit = RetrofitApi.getRetrofit()
        val categoryCall = retrofit.create(CallRestaurantCategory::class.java)
        val call = categoryCall.getCategoryId(categoryId)

        call.enqueue(object : Callback<Categories> {
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                if (response.isSuccessful) {
                    response.body()?.let { category ->
                        textView.text = category.category.toString()


                        Log.e("cat", category.category )
                    }
                } else {
                    Log.e("API_CALL", "Failed to fetch category: ${response.code()}")

                }
            }

            override fun onFailure(call: Call<Categories>, t: Throwable) {
                Log.e("API_CALL", "Failed to fetch category", t)

            }
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val restaurantItems = listItems[position]

        holder.name.text = restaurantItems.name
        loadCategoryName(holder.category, restaurantItems.category_id) // Fetch and display category name
        Log.e("cat2", restaurantItems.category_id.toString())


        holder.cardRestaurantList.setOnClickListener{
            val intent = Intent(context, RestaurantInfoActivity::class.java)
            intent.putExtra("RestaurantId", restaurantItems.id)
            context.startActivity(intent)
        }
    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.text_view_rest_name)
        val category: TextView = view.findViewById(R.id.tv_rest_category)
        val cardRestaurantList: CardView = view.findViewById(R.id.card_restaurant_list)
    }
}
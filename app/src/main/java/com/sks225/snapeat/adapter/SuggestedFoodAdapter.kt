package com.sks225.snapeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sks225.snapeat.R

class SuggestedFoodAdapter(private var foodList: Map<String,String>) : RecyclerView.Adapter<SuggestedFoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_food_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val food = foodList.entries.[position]
        //holder.bind(food)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.food_image)
        private val textView: TextView = itemView.findViewById(R.id.tv_food)

    }
}

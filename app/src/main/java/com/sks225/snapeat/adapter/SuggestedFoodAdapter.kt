package com.sks225.snapeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sks225.snapeat.R

class SuggestedFoodAdapter(private var foodList: Map<String, String>) :
    RecyclerView.Adapter<SuggestedFoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_food_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodList.entries.toList()[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun setData(newList: Map<String, String>) {
        foodList = newList
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.snap_item_image)
        private val textView: TextView = itemView.findViewById(R.id.snap_food_name)

        fun bind(food: Map.Entry<String, String>) {
            Glide.with(itemView).load(food.value).into(imageView)
            textView.text = food.key
        }
    }
}

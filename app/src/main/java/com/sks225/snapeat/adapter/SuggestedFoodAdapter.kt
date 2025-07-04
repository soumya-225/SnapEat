package com.sks225.snapeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sks225.snapeat.R

class SuggestedFoodAdapter(private var foodMap: Map<String, Int>) :
    RecyclerView.Adapter<SuggestedFoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_food_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodMap.entries.toList()[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int {
        return foodMap.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.snap_item_image)
        private val textView: TextView = itemView.findViewById(R.id.snap_food_name)

        fun bind(food: Map.Entry<String, Int>) {
            imageView.setImageResource(food.value)
            textView.text = food.key
        }
    }
}

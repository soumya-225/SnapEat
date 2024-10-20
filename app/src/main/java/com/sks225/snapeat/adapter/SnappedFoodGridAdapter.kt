package com.sks225.snapeat.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sks225.snapeat.R

class SnappedFoodGridAdapter(private val context: Context, private val items: List<Model>) :
    RecyclerView.Adapter<SnappedFoodGridAdapter.ViewHolder>() {
    data class Model(
        val imageUri: Uri,
        val foodName: String,
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.snap_item_image)
        val foodName: TextView = view.findViewById(R.id.snap_food_name)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_snap_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageURI(items[position].imageUri)
        holder.foodName.text = items[position].foodName
    }

    override fun getItemCount() = items.size
}
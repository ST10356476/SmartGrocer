package com.teamvault.smartgrocer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamvault.smartgrocer.R
import com.teamvault.smartgrocer.model.Product

class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val priceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "R${product.price}"
        holder.categoryTextView.text = product.category
        holder.statusTextView.text = if (product.isAvailable) "In Stock" else "Out of Stock"

        if (!product.imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(product.imageUrl).into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image)
        }
    }

    override fun getItemCount(): Int = productList.size
}


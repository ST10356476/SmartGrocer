package com.teamvault.smartgrocer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.teamvault.smartgrocer.adapters.ProductAdapter
import com.teamvault.smartgrocer.databinding.ActivityProductListBinding
import com.teamvault.smartgrocer.model.Product

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var database: DatabaseReference
    private lateinit var productAdapter: ProductAdapter
    private lateinit var allProducts: ArrayList<Product>
    private lateinit var filteredProducts: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "SmartGrocer - Products"

        // Firebase reference
        database = FirebaseDatabase.getInstance().getReference("products")

        allProducts = ArrayList()
        filteredProducts = ArrayList()

        productAdapter = ProductAdapter(filteredProducts)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.setHasFixedSize(true)
        binding.recyclerViewProducts.adapter = productAdapter

        setupCategoryFilter()
        loadProducts()

        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    private fun setupCategoryFilter() {
        val categories = arrayOf("All Categories", "Fruits", "Vegetables", "Dairy", "Bakery", "Meat", "Beverages", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategoryFilter.adapter = adapter

        binding.spinnerCategoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = if (position == 0) "" else categories[position]
                filterProducts(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun filterProducts(category: String) {
        filteredProducts.clear()
        if (category.isEmpty()) {
            filteredProducts.addAll(allProducts)
        } else {
            filteredProducts.addAll(allProducts.filter { it.category == category })
        }
        productAdapter.notifyDataSetChanged()
        binding.textViewEmpty.visibility = if (filteredProducts.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun loadProducts() {
        binding.progressBar.visibility = View.VISIBLE
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allProducts.clear()
                for (productSnap in snapshot.children) {
                    val product = productSnap.getValue(Product::class.java)
                    product?.let { allProducts.add(it) }
                }
                filterProducts(if (binding.spinnerCategoryFilter.selectedItemPosition == 0) "" else binding.spinnerCategoryFilter.selectedItem.toString())
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                binding.textViewEmpty.text = "Error: ${error.message}"
                binding.textViewEmpty.visibility = View.VISIBLE
            }
        })
    }
}

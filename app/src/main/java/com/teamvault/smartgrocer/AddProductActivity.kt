package com.teamvault.smartgrocer

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.teamvault.smartgrocer.databinding.ActivityAddProductBinding
import com.teamvault.smartgrocer.model.Product
import java.util.*

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Product"

        val categories = arrayOf("Fruits", "Vegetables", "Dairy", "Bakery", "Meat", "Beverages", "Other")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = categoryAdapter

        binding.btnAddProduct.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val priceText = binding.editTextPrice.text.toString().trim()
            val category = binding.spinnerCategory.selectedItem.toString()
            val isAvailable = binding.switchAvailability.isChecked
            val imageUrl = binding.editTextImageUrl.text.toString().trim().ifEmpty { null }

            if (name.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Please enter name and price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceText.toDoubleOrNull()
            if (price == null) {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = UUID.randomUUID().toString()
            val product = Product(id, name, price, category, isAvailable, imageUrl.toString())

            val dbRef = FirebaseDatabase.getInstance().getReference("products").child(id)
            dbRef.setValue(product).addOnSuccessListener {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

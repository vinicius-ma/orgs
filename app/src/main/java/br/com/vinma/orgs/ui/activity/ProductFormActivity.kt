package br.com.vinma.orgs.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.R
import br.com.vinma.orgs.dao.ProductsDao
import br.com.vinma.orgs.model.Product

class ProductFormActivity : AppCompatActivity(R.layout.activity_product_form) {

    private val dao: ProductsDao = ProductsDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configButtonSave()
        requestFocusToNameEt()
    }

    private fun requestFocusToNameEt() {
        val nameEt = findViewById<EditText>(R.id.activity_product_form_name)
        nameEt.requestFocus()
    }

    private fun configButtonSave() {
        val saveButton: Button = findViewById(R.id.activity_product_form_button_save)
        saveButton.setOnClickListener {
            val product = createProduct()
            dao.add(product)
            finish()
        }
    }

    private fun createProduct(): Product {
        val nameEt: EditText = findViewById(R.id.activity_product_form_name)
        val descrEt: EditText = findViewById(R.id.activity_product_form_descr)
        val priceEt: EditText = findViewById(R.id.activity_product_form_price)

        val name = nameEt.text.toString()
        val descr = descrEt.text.toString()
        val priceString = priceEt.text.toString()

        var price = 0.0
        if (priceString.isNotBlank()) price = priceString.toDouble()

        return Product(name, descr, price)
    }
}
package br.com.vinma.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductsListBinding
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants
import br.com.vinma.orgs.ui.recyclerview.adapter.ProductListAdapter
import java.math.BigDecimal

class ProductsListActivity: AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var dao: ProductsDao
    private val adapter = ProductListAdapter(this)
    private val binding by lazy { ActivityProductsListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = AppDatabase.instance(this)
        dao = db.productsDao()
        configureAdapter()
        configureFab()
        insertTestDataIfEmpty(dao,10)
    }

    private fun insertTestDataIfEmpty(dao: ProductsDao, numberOfProducts: Int) {
        if(!dao.getAll().isEmpty()) return

        for(i in 1..numberOfProducts) {
            val price = 10 + i + i/100.0
            val description = LoremIpsum(200 + 10 * i).values.toList()[0]
            dao.add(Product("Product $i", description, BigDecimal(price),
                "https://media.tenor.com/_ug_rmdmfhIAAAAS/vegetables.gif"))
        }

        adapter.update(dao.getAll())
    }

    override fun onResume() {
        super.onResume()
        adapter.update(dao.getAll())
    }

    private fun configureFab() {
        binding.activityProductFormButtonSave.setOnClickListener {
            startProductFormActivity()
        }
    }

    private fun startProductFormActivity() {
        val intent = Intent(this, ProductFormActivity::class.java)
        startActivity(intent)
    }

    private fun configureAdapter() {
        val recyclerView = binding.activityProductListRecyclerview
        recyclerView.adapter = adapter

        adapter.onItemClickListener = {position ->
            val intent = Intent(
                this@ProductsListActivity,
                ProductDetailsActivity::class.java
            ).apply {
                val productId = adapter.positionToId(position)
                putExtra(Constants.KEY_PRODUCT_ID, productId)
            }
            startActivity(intent)
        }

    }
}
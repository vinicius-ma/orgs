package br.com.vinma.orgs.ui.activity.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.vinma.orgs.R
import br.com.vinma.orgs.dao.ProductsDao
import br.com.vinma.orgs.ui.recyclerview.adapter.ProductListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsListActivity: AppCompatActivity() {

    private val dao = ProductsDao()
    private val adapter = ProductListAdapter(this, dao.getAll())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)
        configureAdapter()
        configureFab()
    }

    override fun onResume() {
        super.onResume()
        adapter.update(dao.getAll())
    }

    private fun configureFab() {
        findViewById<FloatingActionButton>(R.id.activity_main_fab_new_product).setOnClickListener {
            startProductFormActivity()
        }
    }

    private fun startProductFormActivity() {
        val intent = Intent(this, ProductFormActivity::class.java)
        startActivity(intent)
    }

    private fun configureAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.activity_main_recyclerview)
        recyclerView.adapter = adapter
    }
}
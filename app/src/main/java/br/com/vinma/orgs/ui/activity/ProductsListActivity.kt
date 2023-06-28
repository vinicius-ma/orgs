package br.com.vinma.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductsListBinding
import br.com.vinma.orgs.ui.recyclerview.adapter.ProductListAdapter

class ProductsListActivity: AppCompatActivity() {

    private val dao = ProductsDao()
    private val adapter = ProductListAdapter(this, dao.getAll())
    private val binding by lazy { ActivityProductsListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureAdapter()
        configureFab()
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
    }
}
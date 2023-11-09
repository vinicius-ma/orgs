package br.com.vinma.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.lifecycleScope
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductsListBinding
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants
import br.com.vinma.orgs.ui.menu.ProductsSortMenu
import br.com.vinma.orgs.ui.recyclerview.adapter.ProductListAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

const val NUMBER_SAMPLE_DATA = 10

class ProductsListActivity: OrgsActivity() {
    private lateinit var db: AppDatabase
    private lateinit var dao: ProductsDao
    private val adapter = ProductListAdapter(this)
    private val binding by lazy { ActivityProductsListBinding.inflate(layoutInflater) }
    private lateinit var menu: ProductsSortMenu
    private val register = getActivityResultRegister()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        db = AppDatabase.instance(this)
        dao = db.productsDao()

        configureToolBar()
        configureAdapter()
        configureFab()
        insertTestDataIfEmpty(dao)
    }

    private fun getActivityResultRegister(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){result ->
            if(result.resultCode == RESULT_OK) {
                result.data?.let {
                    if(it.hasExtra(Constants.KEY_PRODUCT_REMOVED_ID)) {
                        val productId = it.getLongExtra(Constants.KEY_PRODUCT_REMOVED_ID, -1L)
                        adapter.delete(productId)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        menu.executeLastSort()
    }

    private fun configureToolBar() {
        menu = ProductsSortMenu(this, binding.productsListToolbar, adapter)
    }

    private fun insertTestDataIfEmpty(dao: ProductsDao) {
        lifecycleScope.launch {
            dao.getAll().let {
                delay(10000)
                if(it.isEmpty()) {
                    for(i in 1..NUMBER_SAMPLE_DATA) {
                        val price = 10 + i + i/100.0
                        val description = LoremIpsum(200 + 10 * i).values.toList()[0]
                        dao.save(Product("Product $i", description, BigDecimal(price),
                            "https://media.tenor.com/_ug_rmdmfhIAAAAS/vegetables.gif"))
                    }
                }
            }
        }
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
            Intent(
                this@ProductsListActivity,
                ProductDetailsActivity::class.java
            ).let {
                val productId = adapter.positionToId(position)
                it.putExtra(Constants.KEY_PRODUCT_ID, productId)
                register.launch(it)
            }
        }
    }
}
package br.com.vinma.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductDetailsBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants
import br.com.vinma.orgs.ui.menu.ProductEditMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsActivity: OrgsActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var dao: ProductsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar?.hide()

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureButtonBack()

        dao = AppDatabase.instance(this).productsDao()
    }

    private fun configureButtonBack() {
        binding.backButton.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        fulfillWithProductInfo()
    }

    private fun fulfillWithProductInfo() {
        val context = this
        lifecycleScope.launch {
            getProductFromIntent()?.let {
                binding.toolbar.title = it.name
                binding.activityProductDetailsName.text = it.name
                binding.activityProductDetailsDescription.text = it.description
                binding.activityProductDetailsPrice.text = it.formattedPrice()
                binding.activityProductDetailsImage.loadImageOrGifWithFallBacks(context, it.url)
                configureToolbar(it)
            } ?: finish()
        }
    }

    private fun configureToolbar(product: Product) {
        ProductEditMenu(this, product, {
            Intent(this, ProductFormActivity::class.java).apply {
                putExtra(Constants.KEY_PRODUCT_ID, it.id)
                startActivity(this)
            }
        },{
            Intent().apply {
                putExtra(Constants.KEY_PRODUCT_REMOVED_ID, product.id)
                setResult(RESULT_OK, this)
            }
            finish()
        }).setAsToolBar(binding.toolbar)
    }

    private suspend fun getProductFromIntent(): Product? {
        val productId = intent.getLongExtra(Constants.KEY_PRODUCT_ID, -1L)
        return dao.findItemById(productId)
    }
}
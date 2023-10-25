package br.com.vinma.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductDetailsBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants
import br.com.vinma.orgs.ui.dialog.ProductEditMenu

class ProductDetailsActivity: AppCompatActivity() {

    private lateinit var product: Product
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var dao: ProductsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar?.hide()

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.instance(this).productsDao()
    }

    override fun onResume() {
        super.onResume()
        fulfillWithProductInfo()
        configureToolbar()
    }

    private fun fulfillWithProductInfo() {

        product = getProductFromIntent()?: return

        binding.toolbar.title = product.name
        binding.activityProductDetailsName.text = product.name
        binding.activityProductDetailsDescription.text = product.description
        binding.activityProductDetailsPrice.text = product.formattedPrice()
        binding.activityProductDetailsImage.loadImageOrGifWithFallBacks(this, product.url)
    }

    private fun configureToolbar() {
            ProductEditMenu(this, binding.root, product,
                {
                    Intent(this, ProductFormActivity::class.java).apply {
                        putExtra(Constants.KEY_PRODUCT_ID, product.id)
                        startActivity(this)
                    }
                },
                {
                    finish()
                }
            ).setAsToolBar(binding.toolbar)
    }

    private fun getProductFromIntent(): Product? {
        val productId = intent.getLongExtra(Constants.KEY_PRODUCT_ID, -1L)
        return dao.findItemById(productId)
    }
}
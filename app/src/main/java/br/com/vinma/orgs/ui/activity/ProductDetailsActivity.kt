package br.com.vinma.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.R
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductDetailsBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants

private const val TAG = "ProductDetailsActivity"

class ProductDetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var db: AppDatabase
    private lateinit var dao: ProductsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar?.hide()

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.instance(this)
        dao = db.productsDao()

        fullfillWithProductInfo()
        configureToolbar()

    }

    private fun fullfillWithProductInfo() {
        val product: Product? = getProductFromIntent()
        binding.toolbar.title = product?.name
        binding.activityProductDetailsName.text = product?.name
        binding.activityProductDetailsDescription.text = product?.description
        binding.activityProductDetailsPrice.text = product?.formattedPrice()
        binding.activityProductDetailsImage.loadImageOrGifWithFallBacks(this, product?.url)
    }

    private fun configureToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_product_details_edit -> {
                    Log.wtf(TAG, "onOptionsItemSelected: edit")
                }

                R.id.menu_product_details_delete -> {
                    Log.wtf(TAG, "onOptionsItemSelected: delete")
                }
            }
            true

        }
    }

    private fun getProductFromIntent(): Product? {
        val productId = intent.getLongExtra(Constants.KEY_PRODUCT_ID, -1L)
        val product: Product? = dao.findItemById(productId)
        return product
    }
}
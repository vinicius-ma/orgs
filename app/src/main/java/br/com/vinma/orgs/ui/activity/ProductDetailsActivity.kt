package br.com.vinma.orgs.ui.activity

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductDetailsBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants

class ProductDetailsActivity: AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var dao: ProductsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar?.hide()

        val binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.instance(this)
        dao = db.productsDao()

        val product: Product? = getProductFromIntent()
        binding.activityProductDetailsName.text = product?.name
        binding.activityProductDetailsDescription.text = product?.description
        binding.activityProductDetailsPrice.text = product?.formattedPrice()
        binding.activityProductDetailsImage.loadImageOrGifWithFallBacks(this, product?.url)

    }

    private fun getProductFromIntent(): Product? {
        val productId = intent.getLongExtra(Constants.KEY_PRODUCT_ID, -1L)
        val product: Product? = dao.findItemById(productId)
        return product
    }
}
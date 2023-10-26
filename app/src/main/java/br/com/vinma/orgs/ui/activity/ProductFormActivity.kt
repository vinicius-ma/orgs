package br.com.vinma.orgs.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.R
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductFormBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants
import br.com.vinma.orgs.ui.dialog.ImageFormDialog
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity() {

    private lateinit var dao: ProductsDao
    private var productId: Long = -1L
    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }
    private var url: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.activity_product_form_new_title)

        dao = AppDatabase.instance(this).productsDao()

        loadProduct()
        configButtonSave()
        requestFocusToNameEt()
        configImageViewClick()
    }

    private fun loadProduct() {
        productId = intent.getLongExtra(Constants.KEY_PRODUCT_ID, -1L)
        val product = dao.findItemById(productId)

        product?.let {
            fulfillFormWithProductInfo(it)
        }
    }

    private fun fulfillFormWithProductInfo(product: Product) {
        url = product.url
        supportActionBar?.title = getString(R.string.activity_product_form_edit_title)
        binding.activityProductFormName.setText(product.name)
        binding.activityProductFormDescr.setText(product.description)
        binding.activityProductFormPrice.setText("${product.price}")
        binding.activityProductFormImage.loadImageOrGifWithFallBacks(this, product.url)
    }

    private fun configButtonSave() {
        val saveButton: Button = binding.activityProductFormButtonSave
        saveButton.setOnClickListener {
            val product = createProduct()
            dao.save(product)
            finish()
        }
    }

    private fun requestFocusToNameEt() {
        binding.activityProductFormName.requestFocus()
    }

    private fun configImageViewClick() {
        binding.activityProductFormImage.setOnClickListener {
            ImageFormDialog(this).show(this.url) {url ->
                this.url = url
                binding.activityProductFormImage.loadImageOrGifWithFallBacks(this, url)
            }
        }
    }

    private fun createProduct(): Product {
        val nameEt = binding.activityProductFormName
        val descrEt = binding.activityProductFormDescr
        val priceEt = binding.activityProductFormPrice
        binding.activityProductFormImage.loadImageOrGifWithFallBacks(this, url)

        val name = nameEt.text.toString()
        val descr = descrEt.text.toString()
        val priceString = priceEt.text.toString()

        var price = BigDecimal.ZERO
        if (priceString.isNotBlank()) price = BigDecimal(priceString)

        return if(productId>-1L){
            Product(name, descr, price, url, productId)
        } else{
            Product(name, descr, price, url)
        }
    }
}
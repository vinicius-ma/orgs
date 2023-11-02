package br.com.vinma.orgs.ui.activity

import android.os.Bundle
import android.widget.Button
import br.com.vinma.orgs.R
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductFormBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants
import br.com.vinma.orgs.ui.dialog.ImageFormDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class ProductFormActivity : OrgsActivity() {

    private lateinit var dao: ProductsDao
    private var productId: Long = -1L
    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }
    private var url: String? = null

    private val nameEt by lazy {binding.activityProductFormName}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        dao = AppDatabase.instance(this).productsDao()

        loadProduct()
        configureButtons()
        requestFocusToNameEt()
        configImageViewClick()
    }

    private fun configureButtons() {
        configureButtonSave()
        configureButtonBack()
    }

    private fun loadProduct() {
        productId = intent.getLongExtra(Constants.KEY_PRODUCT_ID, -1L)

        scope.launch {
            dao.findItemById(productId)?.let {
                withContext(Dispatchers.Main) {
                    fulfillFormWithProductInfo(it)
                    binding.toolbar.title = getString(R.string.activity_product_form_edit_title)
                }
            }
            withContext(Dispatchers.Main){
                nameEt.setSelection(nameEt.length())
            }
        }
    }

    private fun fulfillFormWithProductInfo(product: Product) {
        url = product.url
        supportActionBar?.title = getString(R.string.activity_product_form_edit_title)
        nameEt.setText(product.name)
        binding.activityProductFormDescr.setText(product.description)
        binding.activityProductFormPrice.setText("${product.price}")
        binding.activityProductFormImage.loadImageOrGifWithFallBacks(this, product.url)
    }

    private fun configureButtonBack(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun configureButtonSave() {
        val saveButton: Button = binding.activityProductFormButtonSave
        saveButton.setOnClickListener {
            val product = createProduct()
            scope.launch {
                dao.save(product)
                finish()
            }
        }
    }

    private fun requestFocusToNameEt() {
        val nameEt = nameEt
        nameEt.requestFocus()
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
        val nameEt = nameEt
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
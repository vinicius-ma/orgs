package br.com.vinma.orgs.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductFormBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.dialog.ImageFormDialog
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity() {

    private val dao: ProductsDao = ProductsDao()
    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Novo produto"
        configButtonSave()
        requestFocusToNameEt()
        configImageViewClick()
    }

    private fun configButtonSave() {
        val saveButton: Button = binding.activityProductFormButtonSave
        saveButton.setOnClickListener {
            val product = createProduct()
            dao.add(product)
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

        return Product(name, descr, price, url)
    }
}
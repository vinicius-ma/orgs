package br.com.vinma.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.R
import br.com.vinma.orgs.dao.ProductsDao
import br.com.vinma.orgs.databinding.ActivityProductFormBinding
import br.com.vinma.orgs.databinding.DialogFormImageLoadBinding
import br.com.vinma.orgs.model.Product
import coil.load
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity() {

    private val dao: ProductsDao = ProductsDao()
    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Novo produto"
        configButtonSave()
        requestFocusToNameEt()
        configImageViewClick()

        binding.activityProductFormImage.bringToFront()
        binding.activityProductFormImage.setOnClickListener {
            val inflate = DialogFormImageLoadBinding.inflate(layoutInflater)

            inflate.dialogFormImageLoadButton.setOnClickListener {
                val url = inflate.dialogFormImageLoadUrl.text.toString()
                inflate.dialogFormImageLoadImageView.load(url)
            }

            AlertDialog.Builder(this)
                .setView(inflate.root)
                .setPositiveButton("Confirmar"){_,_ ->}
                .setNegativeButton("Cancelar"){_,_ ->}
                .show()
        }

    }

    private fun configButtonSave() {
        val saveButton: Button = binding.activityProductFormButtonSave
        saveButton.setOnClickListener {
            val product = createProduct()
            dao.add(product)
            Log.e("TAG", "configButtonSave: dao.all = ${dao.getAll()}")
            finish()
        }
    }

    private fun requestFocusToNameEt() {
        binding.activityProductFormName.requestFocus()
    }

    private fun configImageViewClick() {
        binding.activityProductFormImage.isClickable = true
    }

    private fun createProduct(): Product {
        val nameEt = binding.activityProductFormName
        val descrEt = binding.activityProductFormDescr
        val priceEt = binding.activityProductFormPrice

        Log.e("TAG", "createProduct: ${nameEt.text}, ${descrEt.text}, ${priceEt.text}")

        val name = nameEt.text.toString()
        val descr = descrEt.text.toString()
        val priceString = priceEt.text.toString()

        var price = BigDecimal.ZERO
        if (priceString.isNotBlank()) price = BigDecimal(priceString)

        return Product(name, descr, price)
    }
}
package br.com.vinma.orgs.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import br.com.vinma.orgs.databinding.ActivityProductDetailsBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product

class ProductDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar?.hide()

        val binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product: Product? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("KEY_PRODUCT", Product::class.java)
        } else ({
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("KEY_PRODUCT")
        }) as Product?

        binding.activityProductDetailsName.text = product?.name
        binding.activityProductDetailsDescription.text = product?.description
        binding.activityProductDetailsPrice.text = product?.formattedPrice()
        binding.activityProductDetailsImage.loadImageOrGifWithFallBacks(this, product?.url)

    }
}
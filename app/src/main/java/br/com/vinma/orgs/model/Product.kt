package br.com.vinma.orgs.model

import java.io.Serializable
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

data class Product(
    val name: String,
    val description: String,
    val price: BigDecimal,
    val url: String? = null
): Serializable{

    fun formattedPrice(): String? {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("BRL")
        return formatter.format(price)
    }
}

package br.com.vinma.orgs.model

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

data class Product(
    val name: String,
    val description: String,
    val price: BigDecimal,
){

    fun formattedPrice(): String? {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("BRL")
        return formatter.format(price)
    }
}

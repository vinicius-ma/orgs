package br.com.vinma.orgs.model

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

data class Product(
    var name: String? = "",
    var description: String? = "",
    var price: BigDecimal = BigDecimal.ZERO,
){
    // region constructor
    constructor(
        name: String?,
        description: String?,
        price: Double
    ) : this(name, description, BigDecimal(price))
    // endregion

    fun formattedPrice(): String? {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("BRL")
        return formatter.format(price)
    }
}

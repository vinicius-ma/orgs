package br.com.vinma.orgs.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

@Entity
data class Product(
    val name: String,
    val description: String,
    val price: BigDecimal,
    val url: String? = null,
    @PrimaryKey(autoGenerate=true) var id: Long = 0L
): Serializable{

    fun formattedPrice(): String? {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("BRL")
        return formatter.format(price)
    }
}

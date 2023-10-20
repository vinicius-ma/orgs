package br.com.vinma.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.vinma.orgs.model.Product

@Dao
interface ProductDao {

    @Insert
    fun add(vararg products: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT * FROM Product")
    fun getAll(): List<Product>

}
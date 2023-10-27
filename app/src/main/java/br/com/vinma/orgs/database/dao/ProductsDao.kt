package br.com.vinma.orgs.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.vinma.orgs.model.Product

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg products: Product)

    @Update
    fun update(vararg product: Product)

    @Delete
    fun delete(vararg product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun findItemById(id: Long): Product?

    @Query("SELECT * FROM Product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name ASC")
    fun getSortedNameAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name DESC")
    fun getSortedNameDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description ASC")
    fun getSortedDescriptionAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description DESC")
    fun getSortedDescriptionDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY price ASC")
    fun getSortedPriceAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY price DESC")
    fun getSortedPriceDesc(): List<Product>

}
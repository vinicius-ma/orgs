package br.com.vinma.orgs.database.dao

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
    suspend fun save(vararg products: Product)

    @Update
    suspend fun update(vararg product: Product)

    @Delete
    suspend fun delete(vararg product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun findItemById(id: Long): Product?

    @Query("SELECT * FROM Product")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name ASC")
    suspend fun getSortedNameAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name DESC")
    suspend fun getSortedNameDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description ASC")
    suspend fun getSortedDescriptionAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description DESC")
    suspend fun getSortedDescriptionDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY price ASC")
    suspend fun getSortedPriceAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY price DESC")
    suspend fun getSortedPriceDesc(): List<Product>

}
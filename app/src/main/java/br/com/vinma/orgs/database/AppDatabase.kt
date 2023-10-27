package br.com.vinma.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.vinma.orgs.database.converter.Converter
import br.com.vinma.orgs.database.dao.ProductsDao
import br.com.vinma.orgs.model.Product

@Database(entities = [Product::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

    companion object {
        @Volatile
        private lateinit var db: AppDatabase

        fun instance(context: Context): AppDatabase {
            if(::db.isInitialized) return db

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db")
                .allowMainThreadQueries()
                .build().also {
                    db = it
                }
        }
    }

}
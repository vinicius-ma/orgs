package br.com.vinma.orgs.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.vinma.orgs.database.converter.Converter
import br.com.vinma.orgs.database.dao.ProductDao
import br.com.vinma.orgs.model.Product

@Database(entities = [Product::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

}
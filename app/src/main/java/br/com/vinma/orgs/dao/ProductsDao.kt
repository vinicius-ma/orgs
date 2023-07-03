package br.com.vinma.orgs.dao

import br.com.vinma.orgs.model.Product

class ProductsDao {

    fun add(product: Product) {
        products.add(product)
    }

    fun findItemByPosition(position: Int): Product? {
        if(position < 0 || position >= products.size) return null
        return products[position]
    }

    fun getAll(): List<Product> = products

    companion object {
        private val products = mutableListOf<Product>()
    }
}
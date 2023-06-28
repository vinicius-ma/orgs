package br.com.vinma.orgs.dao

import br.com.vinma.orgs.model.Product

class ProductsDao {

    fun add(product: Product) {
        products.add(product)
    }

    fun getAll(): List<Product> = products

    companion object {
        private val products = mutableListOf<Product>()
    }
}
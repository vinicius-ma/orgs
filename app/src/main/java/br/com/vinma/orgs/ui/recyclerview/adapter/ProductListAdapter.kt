package br.com.vinma.orgs.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.databinding.ProductItemBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants
import br.com.vinma.orgs.ui.activity.ProductFormActivity
import br.com.vinma.orgs.ui.menu.ProductEditMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val HOLDER_BOTTOM_MARGIN_DEFAULT = 0
private const val HOLDER_BOTTOM_MARGIN_LAST = 250

class ProductListAdapter(
    private val context : Context,
    products : List<Product> = emptyList(),
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    lateinit var onItemClickListener: (position: Int) -> Unit

    private val products = products.toMutableList()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun positionToId(position: Int) = products[position].id

    fun idToPosition(id: Long) = products.indexOfFirst { it.id == id }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        setViewHolderBottomMargin(holder, position)
        holder.bind(product)
    }

    private fun setViewHolderBottomMargin(
        holder: ViewHolder,
        position: Int
    ) {
        val params: RecyclerView.LayoutParams =
            holder.itemView.layoutParams as RecyclerView.LayoutParams
        params.bottomMargin = if (position < products.size - 1) {
            HOLDER_BOTTOM_MARGIN_DEFAULT
        } else {
            HOLDER_BOTTOM_MARGIN_LAST
        }
        holder.itemView.layoutParams = params
    }

    fun update(products: List<Product>, notifyDataSetChanged: Boolean = true) {
        this.products.clear()
        this.products.addAll(products)
        if(notifyDataSetChanged) notifyItemRangeChanged(0, products.size)
    }

    fun delete(productId: Long) {
        val position = idToPosition(productId)
        products.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(private val binding: ProductItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val context: Context = binding.root.context
        private lateinit var product: Product
        init {

            val dao = AppDatabase.instance(context).productsDao()

            itemView.setOnClickListener {
                if (::product.isInitialized) onItemClickListener(adapterPosition)
            }
            itemView.setOnLongClickListener {
                val productEditMenu = ProductEditMenu(context, product, {
                        Intent(context, ProductFormActivity::class.java).apply {
                            putExtra(Constants.KEY_PRODUCT_ID, product.id)
                            context.startActivity(this)
                        }
                    },{
                        scope.launch {
                            update(dao.getAll(), false)
                            withContext(Dispatchers.Main){
                                notifyItemRemoved(adapterPosition)
                            }
                        }
                    })
                productEditMenu.showAsPopupMenu(itemView)
                true
            }
        }

        fun bind(product: Product) {
            this.product = product
            binding.productItemName.text = product.name
            binding.productItemDescription.text = product.description
            binding.productItemPrice.text = product.formattedPrice()
            binding.activityProductFormImage.loadImageOrGifWithFallBacks(context, product.url)
        }
    }
}

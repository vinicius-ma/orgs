package br.com.vinma.orgs.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.vinma.orgs.databinding.ProductItemBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks
import br.com.vinma.orgs.model.Product

private const val HOLDER_BOTTOM_MARGIN_DEFAULT = 0
private const val HOLDER_BOTTOM_MARGIN_LAST = 500

class ProductListAdapter(
    private val context : Context,
    products : List<Product> = emptyList(),
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    lateinit var onItemClickListener: (position: Int) -> Unit

    private val products = products.toMutableList()

    fun positionToId(position: Int) = products[position].id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setViewHolderBottomMargin(holder, position)
        val product = products[position]
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

    @SuppressLint("NotifyDataSetChanged")
    fun update(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ProductItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val context: Context = binding.root.context
        private lateinit var product: Product
        init {
            itemView.setOnClickListener {
                if (::product.isInitialized) onItemClickListener(adapterPosition)
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

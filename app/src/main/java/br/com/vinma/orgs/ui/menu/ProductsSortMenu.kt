package br.com.vinma.orgs.ui.menu

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import br.com.vinma.orgs.R
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.model.Product
import br.com.vinma.orgs.ui.Constants.Companion.PREF_LAS_SORT_ID
import br.com.vinma.orgs.ui.Constants.Companion.SHARED_PREFERENCES
import br.com.vinma.orgs.ui.recyclerview.adapter.ProductListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsSortMenu(
    private val context: Context,
    private val toolbar: Toolbar,
    private val adapter: ProductListAdapter
) {

    private val dao = AppDatabase.instance(context).productsDao()
    private val preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        setAsToolBar()
    }

    fun executeLastSort(){
        val lasSortedId = getLastSortedPreference()
        doWhen(lasSortedId)
    }

    private fun setAsToolBar(){
        toolbar.setOnMenuItemClickListener {menuItem ->
            doWhen(menuItem.itemId)
        }
    }

    private fun doWhen(itemId: Int): Boolean{
        var returningValue = true
        var newProducts: List<Product>? = null
        var iconId: Int? = null
        scope.launch {
            when (itemId) {
                R.id.products_sort_default -> {
                    iconId = R.drawable.ic_action_sort
                    newProducts = dao.getAll()
                }
                R.id.products_sort_name_ascending -> {
                    iconId = R.drawable.ic_action_sort_name
                    newProducts = dao.getSortedNameAsc()
                }
                R.id.products_sort_name_descending -> {
                    iconId = R.drawable.ic_action_sort_name
                    newProducts = dao.getSortedNameDesc()
                }
                R.id.products_sort_description_ascending -> {
                    iconId = R.drawable.ic_action_sort_description
                    newProducts = dao.getSortedDescriptionAsc()
                }
                R.id.products_sort_description_descending -> {
                    iconId = R.drawable.ic_action_sort_description
                    newProducts = dao.getSortedDescriptionDesc()
                }
                R.id.products_sort_price_ascending -> {
                    iconId = R.drawable.ic_action_sort_price
                    newProducts = dao.getSortedPriceAsc()
                }
                R.id.products_sort_price_descending -> {
                    iconId = R.drawable.ic_action_sort_price
                    newProducts = dao.getSortedPriceDesc()
                }
                else -> {
                    returningValue = false
                }
            }
            saveLastSortedPreference(itemId)
            withContext(Dispatchers.Main){
                iconId?.let {
                    val menuItem = toolbar.menu.findItem(R.id.products_sort_main)
                    menuItem.icon = AppCompatResources.getDrawable(context, it)
                }
                newProducts?.let{adapter.update(it)}
            }

        }
        return returningValue
    }

    private fun saveLastSortedPreference(menuId: Int){
        preferences?.let {
            val editor = it.edit().putInt(PREF_LAS_SORT_ID, menuId)
            editor.apply()
        }
    }

    private fun getLastSortedPreference(): Int {
        return preferences?.getInt(PREF_LAS_SORT_ID, R.id.products_sort_default) ?: R.id.products_sort_default
    }
}
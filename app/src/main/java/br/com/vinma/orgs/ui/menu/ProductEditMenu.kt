package br.com.vinma.orgs.ui.menu

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import br.com.vinma.orgs.R
import br.com.vinma.orgs.database.AppDatabase
import br.com.vinma.orgs.model.Product

class ProductEditMenu(
    private val context: Context,
    private val product: Product,
    private val onEdit: (product: Product) -> Unit,
    private val onDelete: (product: Product) -> Unit
) {

    fun showAsPopupMenu(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.product_details, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            doWhen(menuItem.itemId)
        }
        popupMenu.show()
    }

    fun setAsToolBar(toolbar: Toolbar){
        toolbar.setOnMenuItemClickListener {menuItem ->
            doWhen(menuItem.itemId)
        }
    }

    private fun doWhen(itemId: Int): Boolean {
        return when (itemId) {
            R.id.menu_product_details_edit -> {
                onEdit(product)
                true
            }
            R.id.menu_product_details_delete -> {
                confirmDeletion()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun confirmDeletion() {
        val alertDialog = configureConfirmationDialog()
        alertDialog?.show()
    }

    private fun configureConfirmationDialog(): AlertDialog.Builder? {
        return AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_delete_product_title))
            .setMessage(context.getString(R.string.dialog_delete_product_message))
            .setPositiveButton(R.string.dialog_delete_product_positive) { _, _ ->
                AppDatabase.instance(context).productsDao().delete(product)
                onDelete(product)
            }
            .setNegativeButton(context.getString(R.string.dialog_delete_product_negative), null)
    }

}
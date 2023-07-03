package br.com.vinma.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.vinma.orgs.databinding.DialogFormImageLoadBinding
import br.com.vinma.orgs.extensions.loadImageOrGifWithFallBacks

class ImageFormDialog(private val context: Context) {

    fun show(
        urlDefault : String? = null,
        onPositiveClick: (url: String) -> Unit
    ){
        DialogFormImageLoadBinding.inflate(LayoutInflater.from(context)).apply {

            urlDefault?.let {
                dialogFormImageLoadImageView.loadImageOrGifWithFallBacks(context, it)
                dialogFormImageLoadUrl.setText(it)
            }

            dialogFormImageLoadButton.setOnClickListener {
                val url = dialogFormImageLoadUrl.text.toString()
                dialogFormImageLoadImageView.loadImageOrGifWithFallBacks(context, url)
            }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar"){_,_ ->
                    val url = dialogFormImageLoadUrl.text.toString()
                    onPositiveClick(url)
                }
                .setNegativeButton("Cancelar"){_,_ ->}
                .show()
        }
    }
}
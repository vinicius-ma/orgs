package br.com.vinma.orgs.extensions

import android.content.Context
import android.widget.ImageView
import br.com.vinma.orgs.R
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.load
import com.google.android.material.color.utilities.MaterialDynamicColors.background

fun ImageView.loadImageOrGifWithFallBacks(context: Context,url:String?=null){
    load(url, imageLoaderWithGifs(context) ){
        fallback(R.drawable.ic_image_add_adapter_foreground)
        error(R.drawable.ic_image_broken_foreground)
        placeholder(R.drawable.ic_image_loading_foreground)
    }
}

private fun imageLoaderWithGifs(context:Context) = ImageLoader.Builder(context).components {
    add(ImageDecoderDecoder.Factory())
}.build()
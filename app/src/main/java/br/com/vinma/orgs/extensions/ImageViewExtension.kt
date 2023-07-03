package br.com.vinma.orgs.extensions

import android.content.Context
import android.widget.ImageView
import br.com.vinma.orgs.R
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.load

fun ImageView.loadImageOrGifWithFallBacks(context: Context,url:String?=null){
    load(url, imageLoaderWithGifs(context) ){
        fallback(R.mipmap.ic_image_broken)
        error(R.mipmap.ic_image_broken)
        placeholder(R.mipmap.ic_image_loading)
    }
}

private fun imageLoaderWithGifs(context:Context) = ImageLoader.Builder(context).components {
    add(ImageDecoderDecoder.Factory())
}.build()
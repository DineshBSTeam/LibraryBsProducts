package com.lib.bslibrary.internal.helper

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.appcompat.widget.AppCompatImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.lib.bslibrary.R
import com.bumptech.glide.Glide
import kotlin.math.roundToInt


class Util {

    fun loadNetworkImage(context: Context, url: String, imageview: AppCompatImageView) {
        if (url.contains(".svg")) {
            imageview.loadSvg(context, url)
        } else {
            Glide.with(context).load(url).placeholder(R.drawable.bs_logo).dontAnimate()
                .into(imageview).clearOnDetach()
        }
    }

    private fun AppCompatImageView.loadSvg(context: Context, url: String) {
        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        val request = ImageRequest.Builder(context)
            .crossfade(true)
            .crossfade(500)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    fun dpToPx(dp: Double): Int {
        return (dp * (Resources.getSystem()
            .displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun toCorrectMobileNo(str:String): String {
        var newString = str
        val n = 10
        if (str.length > n) {
            newString = str.substring(str.length - n)
        }
        return newString
    }
}
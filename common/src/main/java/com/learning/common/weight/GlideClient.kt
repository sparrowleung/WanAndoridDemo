package com.learning.common.weight

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.learning.common.R

class GlideClient {
    companion object {
        private var width = 0
        private var height = 0
        fun create(context: Context, url: String, view: ImageView) {
            width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                100.toFloat(),
                context.resources.displayMetrics
            ).toInt()
            height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                120.toFloat(),
                context.resources.displayMetrics
            ).toInt()
            if (url.endsWith("gif")) {
                Glide.with(context).loadUrlAsGif(url, view)
            } else {
                Glide.with(context).loadUrlAsDrawable(url, view)
            }

        }

        private fun RequestManager.loadUrlAsDrawable(url: String, view: ImageView) {
            this.asDrawable()
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .load(url)
                .thumbnail(0.1f)
                .into(view)
        }

        private fun RequestManager.loadUrlAsGif(url: String, view: ImageView) {
            this.asGif()
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .load(url)
                .thumbnail(0.1f)
                .into(view)
        }
    }

}
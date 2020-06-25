package com.learning.demomode

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.learning.common.weight.GlideClient

object LoadImageHelper {
    @BindingAdapter("app:projectUrl")
    @JvmStatic
    fun setProjectUrl(imageView: ImageView, url: String) {
        GlideClient.create(imageView.context, url, imageView)
    }

    @BindingAdapter("app:homeLike")
    @JvmStatic
    fun setHomeLike(imageView: ImageView, state: Boolean) {
        if (state) Glide.with(imageView.context)
            .load(R.drawable.like_filled).into(imageView)
    }
}
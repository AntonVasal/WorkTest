package com.example.test.utils.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.test.R

@BindingAdapter("imageUrl")
fun imageUrl(view: ImageView, imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(view.context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_user_main)
            .error(R.drawable.ic_user_main)
            .into(view)
    }
}

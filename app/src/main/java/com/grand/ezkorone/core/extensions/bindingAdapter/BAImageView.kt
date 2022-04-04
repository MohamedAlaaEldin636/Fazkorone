package com.grand.ezkorone.core.extensions.bindingAdapter

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.grand.ezkorone.R

@BindingAdapter("imageView_setUrlViaGlide")
fun ImageView.setUrlViaGlide(url: String?) {
	if (url.isNullOrEmpty()) {
		setImageDrawable(null)
	}else {
		Glide.with(context)
			.load(url)
			.apply(RequestOptions().centerCrop())
			.placeholder(R.drawable.ic_placeholder)
			.error(R.drawable.ic_placeholder)
			.into(this)
	}
}

@BindingAdapter("imageView_setUrlViaGlideOrIgnore")
fun ImageView.setUrlViaGlideOrIgnore(url: String?) {
	if (!url.isNullOrEmpty()) {
		Glide.with(context)
			.load(url)
			.apply(RequestOptions().centerCrop())
			.placeholder(R.drawable.ic_placeholder)
			.error(R.drawable.ic_placeholder)
			.into(this)
	}
}

@BindingAdapter("imageView_setViaGlideUsingUriOrIgnore")
fun ImageView.setViaGlideUsingUriOrIgnore(uri: Uri?) {
	if (uri != null) {
		Glide.with(context)
			.load(uri)
			.apply(RequestOptions().centerCrop())
			.placeholder(R.drawable.ic_placeholder)
			.error(R.drawable.ic_placeholder)
			.into(this)
	}
}

@BindingAdapter("imageView_setLinkViaGlideOrIgnore_link", "imageView_setLinkViaGlideOrIgnore_isImageNotVideo")
fun ImageView.setLinkViaGlideOrIgnore(link: String?, isImageNotVideo: Boolean?) {
	if (!link.isNullOrEmpty()) {
		val options = RequestOptions().let {
			if (isImageNotVideo == true) it else it.frame(1)
		}.centerCrop()
		Glide.with(context)
			.load(link)
			.apply(options)
			.placeholder(R.drawable.ic_placeholder)
			.error(R.drawable.ic_placeholder)
			.into(this)
	}
}

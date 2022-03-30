package com.grand.ezkorone.core.extensions

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.NavigationRes
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.grand.ezkorone.R
import com.grand.ezkorone.presentation.main.MainActivity

fun Fragment.findNavControllerOfProject(): NavController {
	return Navigation.findNavController(
		requireActivity(),
		R.id.mainNavHostFragment
	)
}

fun View.findNavControllerOfProject(): NavController {
	return findFragment<Fragment>().findNavControllerOfProject()
}

fun View.openDrawerLayout() {
	(findFragment<Fragment>().activity as? MainActivity)?.binding?.drawerLayout?.openDrawer(GravityCompat.START)
}

fun NavController.popAllBackStacks() {
	while (popBackStack()) {
		continue
	}
}

fun NavController.navigateDeepLinkWithoutOptions(scheme: String, authority: String, vararg paths: String) =
	navigateDeepLinkOptionalOptions(scheme, authority, null, *paths)

fun defaultNavOptionsBuilder() = NavOptions.Builder()
	.setEnterAnim(R.anim.anim_slide_in_right)
	.setExitAnim(R.anim.anim_slide_out_left)
	.setPopEnterAnim(R.anim.anim_slide_in_left)
	.setPopExitAnim(R.anim.anim_slide_out_right)

fun NavController.navigateDeepLinkWithOptions(
	scheme: String,
	authority: String,
	vararg paths: String,
	options: NavOptions = NavOptions.Builder()
		.setEnterAnim(R.anim.anim_slide_in_right)
		.setExitAnim(R.anim.anim_slide_out_left)
		.setPopEnterAnim(R.anim.anim_slide_in_left)
		.setPopExitAnim(R.anim.anim_slide_out_right)
		.build(),
) = navigateDeepLinkOptionalOptions(scheme, authority, options, *paths)

private fun NavController.navigateDeepLinkOptionalOptions(
	scheme: String,
	authority: String,
	options: NavOptions?,
	vararg paths: String
) {
	val uri = Uri.Builder()
		.scheme(scheme)
		.authority(authority)
		.let {
			var builder = it
			for (path in paths) {
				builder = builder.appendPath(path)
			}
			builder
		}
		.build()
	val request = NavDeepLinkRequest.Builder.fromUri(uri).build()

	navigate(request, options)
}

fun NavController.inflateGraph(@NavigationRes navigationRes: Int, args: Bundle? = null) {
	try {
		graph
	}catch (e: IllegalStateException) {
		val navGraph = navInflater.inflate(navigationRes)
		setGraph(navGraph, args)
	}
}

package com.grand.ezkorone.core.customTypes

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.transition.TransitionManager
import java.lang.ref.WeakReference

class MAToolbarOnDestinationChangedListener(
	toolbar: Toolbar,
	configuration: AppBarConfiguration
) : MAAbstractAppBarOnDestinationChangedListener(toolbar.context, configuration) {
	private val toolbarWeakReference: WeakReference<Toolbar> = WeakReference(toolbar)
	
	override fun onDestinationChanged(
		controller: NavController,
		destination: NavDestination,
		arguments: Bundle?
	) {
		val toolbar = toolbarWeakReference.get()
		if (toolbar == null) {
			controller.removeOnDestinationChangedListener(this)
			return
		}
		super.onDestinationChanged(controller, destination, arguments)
	}
	
	override fun setTitle(title: CharSequence?) {
		// to-do ignore it isa.
		/*toolbarWeakReference.get()?.let { toolbar ->
			toolbar.title = title
		}*/
	}
	
	override fun setNavigationIcon(icon: Drawable?, @StringRes contentDescription: Int) {
		toolbarWeakReference.get()?.run {
			val useTransition = icon == null && navigationIcon != null
			navigationIcon = icon
			setNavigationContentDescription(contentDescription)
			if (useTransition) {
				TransitionManager.beginDelayedTransition(this)
			}
		}
	}
}

package com.grand.ezkorone.core.extensions

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.grand.ezkorone.R

/*fun View.showPopup(list: List<String>, context: Context = this.context, listener: PopupMenu.OnMenuItemClickListener) {
	val popupMenu = PopupMenu(context, this)
	
	popupMenu.inflate(R.menu.menu_empty)
	for (item in list) {
		popupMenu.menu.add(item)
	}
	
	popupMenu.setOnMenuItemClickListener(listener)
	
	popupMenu.show()
}*/

fun View.showPopup(list: List<String>, context: Context = this.context, listener: (MenuItem) -> Unit) {
	val popupMenu = PopupMenu(context, this)
	
	popupMenu.inflate(R.menu.menu_empty)
	for (item in list) {
		popupMenu.menu.add(item)
	}
	
	popupMenu.setOnMenuItemClickListener {
		listener(it)
		
		false
	}
	
	popupMenu.show()
}

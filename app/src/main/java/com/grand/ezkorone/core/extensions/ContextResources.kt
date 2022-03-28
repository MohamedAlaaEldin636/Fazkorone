package com.grand.ezkorone.core.extensions

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun Context.dpToPx(value: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
    )
}

/** - Layout inflater from `receiver`, by using [LayoutInflater.from] */
val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

/**
 * - Inflates a layout from [layoutRes] isa.
 *
 * @param parent provide [ViewGroup.LayoutParams] to the returned root view, default is `null`
 * @param attachToRoot if true then the returned view will be attached to [parent] if not `null`,
 * default is false isa.
 *
 * @return rootView in the provided [layoutRes] isa.
 */
@JvmOverloads
fun Context.inflateLayout(
    @LayoutRes layoutRes: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View {
    return layoutInflater.inflate(layoutRes, parent, attachToRoot)
}

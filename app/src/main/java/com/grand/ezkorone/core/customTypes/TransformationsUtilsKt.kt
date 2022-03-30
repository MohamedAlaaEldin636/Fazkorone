package com.grand.ezkorone.core.customTypes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

inline fun <X, Y> LiveData<X>.map(crossinline transform: (X) -> Y): MutableLiveData<Y> =
	TransformationsUtils.map(this) { transform(it) }

inline fun <X, Y> LiveData<X>.mapNullable(crossinline transform: (X?) -> Y): MutableLiveData<Y> =
	TransformationsUtils.map(this) { transform(it) }

inline fun <X, Y> LiveData<X>.switchMap(crossinline transform: (X) -> LiveData<out Y>): LiveData<out Y> =
	TransformationsUtils.switchMap(this) { transform(it) }

inline fun <Y> switchMapMultiple(
	vararg liveData: LiveData<*>,
	crossinline transform: () -> LiveData<Y>
): LiveData<Y> {
	return TransformationsUtils.switchMapMultiple(
		{ transform() },
		*liveData
	)
}

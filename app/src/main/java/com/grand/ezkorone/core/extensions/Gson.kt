package com.grand.ezkorone.core.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grand.ezkorone.core.di.module.GsonModule

inline fun <reified E> E?.toJsonOrNull(gson: Gson? = null): String? = kotlin.runCatching {
    toJson<E>(gson)
}.getOrNull()

inline fun <reified E> E?.toJson(gson: Gson? = null): String {
    return (gson ?: GsonModule.provideGson()).toJson(this, object : TypeToken<E>(){}.type)
}

inline fun <reified E> String?.fromJsonOrNull(gson: Gson? = null): E? = kotlin.runCatching {
    fromJson<E>(gson)
}.getOrNull()

inline fun <reified E> String?.fromJson(
    gson: Gson? = null,
): E {
    return (gson ?: GsonModule.provideGson()).fromJson(this, object : TypeToken<E>(){}.type)
}

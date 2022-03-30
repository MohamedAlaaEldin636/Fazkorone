package com.grand.ezkorone.domain.home

import com.google.gson.annotations.SerializedName

data class ItemZekrTopCategory(
    val id: Int,
    val type: Int,
    val name: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("is_favorite") val isFavorite: Boolean
)

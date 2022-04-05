package com.grand.ezkorone.domain.favorite

import com.google.gson.annotations.SerializedName

/**
 * @param type 1 -> home azkar horizontal, 2 -> home azkar vertical
 */
data class ItemFavorite(
    var id: Int,
    var type: Int,
    var name: String,
    @SerializedName("image") var imageUrl: String,
    //"is_favorite": true
)

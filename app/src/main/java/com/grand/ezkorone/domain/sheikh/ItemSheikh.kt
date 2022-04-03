package com.grand.ezkorone.domain.sheikh

import com.google.gson.annotations.SerializedName

data class ItemSheikh(
    var id: Int,
    var name: String,
    @SerializedName("image") var imageUrl: String,
    @SerializedName("audio") var audioUrl: String,
    var selected: Boolean
)

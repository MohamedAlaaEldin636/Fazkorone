package com.grand.ezkorone.domain.taspeh

import com.google.gson.annotations.SerializedName

data class ItemTaspeh(
    var id: Int,
    var name: String,
    @SerializedName("audio") var audioUrl: String,
    @SerializedName("praise_limit") var count: Int,
)

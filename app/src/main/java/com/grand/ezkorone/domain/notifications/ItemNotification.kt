package com.grand.ezkorone.domain.notifications

import com.google.gson.annotations.SerializedName

data class ItemNotification(
    var id: Int,
    var message: String,
    @SerializedName("created_at") var createdAt: String,
)

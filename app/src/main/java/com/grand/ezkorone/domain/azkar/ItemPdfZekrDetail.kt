package com.grand.ezkorone.domain.azkar

import com.google.gson.annotations.SerializedName

/**
 * @param type 1 means all pdfs, 2 means each one isa.
 */
data class ItemPdfZekrDetail(
    var id: Int,
    var type: Int,
    @SerializedName("file") var pdfUrl: String,
    @SerializedName("sound") var audioUrl: String,
    @SerializedName("audio_numbers") var maxCount: Int,
    @SerializedName("is_favorite") var isFavorite: Boolean,
)

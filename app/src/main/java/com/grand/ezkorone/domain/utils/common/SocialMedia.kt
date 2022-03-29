package com.grand.ezkorone.domain.utils.common

import com.google.gson.annotations.SerializedName

/*
{
        "id": 1,
        "instegram": "https://2grand.net/",
        "twitter": "https://2grand.net/",
        "about_us_image": "https://fazkrony.my-staff.net/images/settings/bg.png"
    }
 */
/**
 * @param instaUrl just launch like any url link any https link via intent isa.
 * @param twitterUrl see [instaUrl] isa.
 */
data class SocialMedia(
    val id: Int,
    @SerializedName("instegram") val instaUrl: String,
    @SerializedName("twitter") val twitterUrl: String,
    @SerializedName("about_us_image") val aboutUsImageUrl: String,
)

package com.grand.ezkorone.domain.utils

import com.google.gson.annotations.SerializedName
import com.grand.ezkorone.domain.utils.common.SocialMedia

data class MABaseResponse<T>(
	val data: T?,
	val message: String,
	val code: Int,
	@SerializedName("social_media") val socialMedia: SocialMedia?
)

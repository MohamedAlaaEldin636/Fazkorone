package com.grand.ezkorone.domain.utils

import com.google.gson.annotations.SerializedName
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.domain.utils.common.SocialMedia

data class MABaseResponse<T>(
	val data: T?,
	val message: String,
	@SerializedName("status") val code: Int,
	@SerializedName("social_media") val socialMedia: SocialMedia?,
	@SerializedName("azkar_categories") val azkarCategories: List<IdAndName>?
) {

	fun <R> mapData(conversion: (T?) -> R?): MABaseResponse<R> {
		return MABaseResponse(conversion(data), message, code, socialMedia, azkarCategories)
	}

	/*
	"code":200,"status":"OK"
	 */

}

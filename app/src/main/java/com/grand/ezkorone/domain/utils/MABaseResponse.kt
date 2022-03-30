package com.grand.ezkorone.domain.utils

import com.google.gson.annotations.SerializedName
import com.grand.ezkorone.domain.utils.common.SocialMedia

data class MABaseResponse<T>(
	val data: T?,
	val message: String,
	@SerializedName("status") val code: Int,
	@SerializedName("social_media") val socialMedia: SocialMedia?
) {

	fun <R> mapData(conversion: (T?) -> R?): MABaseResponse<R> {
		return MABaseResponse(conversion(data), message, code, socialMedia)
	}

	/*
	"code":200,"status":"OK"
	 */

}

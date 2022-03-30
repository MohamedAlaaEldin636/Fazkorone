package com.grand.ezkorone.domain.utils

import com.google.gson.annotations.SerializedName
import com.grand.ezkorone.domain.utils.common.SocialMedia

data class MABaseResponseAladan<T>(
    val data: T?,
    @SerializedName("status") val message: String?,
    val code: Int,
) {

    fun toMABaseResponse(): MABaseResponse<T> {
        return MABaseResponse(data, message.orEmpty(), code, null, null)
    }

}

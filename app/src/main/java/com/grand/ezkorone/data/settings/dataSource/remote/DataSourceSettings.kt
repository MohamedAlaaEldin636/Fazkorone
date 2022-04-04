package com.grand.ezkorone.data.settings.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import javax.inject.Inject

class DataSourceSettings @Inject constructor(
    private val apiService: ApiSettingsServices,
) : MABaseRemoteDataSource() {

    suspend fun getInitialAppRequirementsAndSetFirebaseToken(firebaseToken: String) = safeApiCall {
        apiService.getInitialAppRequirementsAndSetFirebaseToken(firebaseToken)
    }

    suspend fun getContactUsPurposes() = safeApiCall {
        apiService.getContactUsPurposes()
    }

    suspend fun contactUs(
        name: String,
        email: String,
        purposeId: Int,
        message: String,
        image: MultipartBody.Part? = null,
    ) = safeApiCall {
        if (image != null) {
            apiService.contactUs(image, name.toRequestBody(), email.toRequestBody(), purposeId, message.toRequestBody())
        }else {
            apiService.contactUs(name.toRequestBody(), email.toRequestBody(), purposeId, message.toRequestBody())
        }
    }

}

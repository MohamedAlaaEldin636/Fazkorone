package com.grand.ezkorone.data.settings.dataSource.remote

import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DataSourceSettings @Inject constructor(
    private val apiService: ApiSettingsServices,
) : MABaseRemoteDataSource() {

    suspend fun getInitialAppRequirementsAndSetFirebaseToken(firebaseToken: String) = safeApiCall {
        apiService.getInitialAppRequirementsAndSetFirebaseToken(firebaseToken)
    }

}

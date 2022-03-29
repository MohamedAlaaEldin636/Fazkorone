package com.grand.ezkorone.data.settings.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.messaging.FirebaseMessaging
import com.grand.ezkorone.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.settings.dataSource.remote.DataSourceSettings
import com.grand.ezkorone.domain.aboutUs.ItemAboutUs
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MAResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RepositorySettings @Inject constructor(
    private val dataSource: DataSourceSettings,
    private val prefsApp: PrefsApp,
) {

    fun getInitialAppRequirementsAndSetFirebaseToken() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<ItemAboutUs>>> {
        val firebaseToken = getCurrentFirebaseToken()

        val token = firebaseToken.second.orEmpty()

        if (token.isNotEmpty()) {
            prefsApp.setFirebaseToken(token)

            emit(dataSource.getInitialAppRequirementsAndSetFirebaseToken(token))
        }else {
            Timber.d("Firebase error with exception ${firebaseToken.first}")

            emit(MAResult.Failure(MAResult.Failure.Status.ERROR, null, firebaseToken.first?.message))
        }
    }

    private suspend fun getCurrentFirebaseToken(): Pair<Exception?, String?> {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.d("Firebase Token exception -> ${task.exception}")
                    continuation.resume(task.exception to null)
                }else {
                    Timber.d("Firebase Token -> ${task.result}")
                    continuation.resume(null to task.result)
                }
            }
        }
    }

}

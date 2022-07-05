package com.grand.ezkorone.data.notifications.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.grand.ezkorone.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.grand.ezkorone.data.basePaging.BasePaging
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.notifications.dataSource.remote.DataSourceNotifications
import com.grand.ezkorone.data.settings.dataSource.remote.DataSourceSettings
import com.grand.ezkorone.domain.aboutUs.ItemAboutUs
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MAResult
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RepositoryNotifications @Inject constructor(
    private val dataSource: DataSourceNotifications,
    private val prefsApp: PrefsApp,
) {

    fun enableOrDisableNotifications(enable: Boolean) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<Any>> {
        emit(dataSource.enableOrDisableNotifications(enable))
    }

    suspend fun enableOrDisableNotificationsSuspend(enable: Boolean) = dataSource.enableOrDisableNotifications(enable)

    fun registerDevice() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<Any>> {
        val firebaseToken = getCurrentFirebaseToken()

        val token = firebaseToken.second.orEmpty()

        if (token.isNotEmpty()) {
            prefsApp.setFirebaseToken(token)

            emit(dataSource.registerDevice())
        }else {
            Timber.d("Firebase error with exception ${firebaseToken.first}")

            emit(MAResult.Failure(MAResult.Failure.Status.ERROR, null, firebaseToken.first?.message))
        }
    }

    fun getNotifications() = BasePaging.createFlowViaPager {
        dataSource.getNotifications()
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

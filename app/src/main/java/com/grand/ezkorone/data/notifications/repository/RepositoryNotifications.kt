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
) {

    fun enableOrDisableNotifications(enable: Boolean) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<Any>> {
        emit(dataSource.enableOrDisableNotifications(enable))
    }

    fun registerDevice() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<Any>> {
        emit(dataSource.registerDevice())
    }

    fun getNotifications() = BasePaging.createFlowViaPager {
        dataSource.getNotifications()
    }

}

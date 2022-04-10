package com.grand.ezkorone.workManager

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.Observer
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.grand.ezkorone.R
import com.grand.ezkorone.broadcastReceiver.SalawatAlarmsBroadcastReceiver
import com.grand.ezkorone.core.customTypes.GlobalError
import com.grand.ezkorone.core.extensions.executeOnGlobalLoadingAndAutoHandleRetry
import com.grand.ezkorone.core.extensions.toLocalDateTimeUTCOffset
import com.grand.ezkorone.data.azan.repository.RepositoryAzan
import com.grand.ezkorone.domain.alarms.TimeInDay
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MAResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import timber.log.Timber
import java.time.LocalDateTime
import java.time.LocalTime

@HiltWorker
class SalawatAlarmsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repoAzan: RepositoryAzan
) : CoroutineWorker(
    appContext,
    workerParams
) {

    companion object {
        const val KEY_INPUT_DATA_TRIGGER_TIME_MILLIS = "KEY_INPUT_DATA_TRIGGER_TIME_MILLIS"
        const val KEY_INPUT_DATA_TRIGGER_TAG = "KEY_INPUT_DATA_TRIGGER_TAG"
        const val UNIQUE_NAME = "com.grand.ezkorone.workManager.SalawatAlarmsWorker.UNIQUE_NAME"
    }

    override suspend fun doWork(): Result {
        Timber.e("TAG TIRJOTRO -> SalawatAlarmsWorker")

        val triggerMillis = inputData.getLong(KEY_INPUT_DATA_TRIGGER_TIME_MILLIS, -1)
        val triggerTag = inputData.getString(KEY_INPUT_DATA_TRIGGER_TAG).orEmpty()

        var triggerDateTime = triggerMillis.toLocalDateTimeUTCOffset()
        val nowDateTime = LocalDateTime.now()
        while (triggerDateTime <= nowDateTime) {
            triggerDateTime = triggerDateTime.plusDays(1)
        }

        val type = SalahFardType.valueOf(triggerTag)

        val calledApiSuccessfully = executeWithRetryAndDelay(
            afterShowingLoading = {
                repoAzan.getAzanTimesSuspend(triggerDateTime.toLocalDate())
            },
            afterHidingLoading = {
                val timeInDay = it.timings.getTimeInDayOf(type)

                triggerDateTime = LocalDateTime.of(
                    triggerDateTime.toLocalDate(),
                    LocalTime.of(timeInDay.hour24, timeInDay.minute)
                )

                SalawatAlarmsBroadcastReceiver.scheduleAlarmManagerAndWorkManager(
                    applicationContext,
                    triggerDateTime,
                    type
                )
            }
        )

        if (!calledApiSuccessfully) {
            SalawatAlarmsBroadcastReceiver.scheduleWorkManagerOnly(
                applicationContext,
                triggerDateTime.plusDays(1),
                type
            )
        }

        return Result.success()
    }

    /**
     * @return `true` if [afterHidingLoading] is called meaning success on [afterShowingLoading]
     * occurred isa.
     */
    private suspend inline fun <T> executeWithRetryAndDelay(
        afterShowingLoading: () -> MAResult.Immediate<MABaseResponse<T>>,
        afterHidingLoading: (T) -> Unit,
        retryTimes: Int = 3,
        delayInMillisBetweenRetries: Long = 1_000,
    ): Boolean {
        repeat(retryTimes) {
            when (val result = afterShowingLoading()) {
                is MAResult.Failure -> {
                    Timber.d("failure is $result")

                    delay(delayInMillisBetweenRetries)
                }
                is MAResult.Success -> {
                    afterHidingLoading(result.value.data!!)

                    return true
                }
            }
        }

        return false
    }

}

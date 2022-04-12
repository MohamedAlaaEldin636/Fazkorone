package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.app.Application
import android.app.DownloadManager
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.grand.ezkorone.R
import com.grand.ezkorone.broadcastReceiver.SalawatAlarmsBroadcastReceiver
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.data.azan.repository.RepositoryAzan
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.local.preferences.PrefsSalah
import com.grand.ezkorone.domain.azan.SalawatTimes
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.domain.salah.SimpleDate
import com.grand.ezkorone.domain.salah.toSimpleDate
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import com.grand.ezkorone.presentation.internalNavigation.SalahFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.time.*
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SalahViewModel @Inject constructor(
    application: Application,
    prefsApp: PrefsApp,
    val prefsSalah: PrefsSalah,
    val repoAzan: RepositoryAzan,
) : AndroidViewModel(application) {

    companion object {
        private const val DIALOG_DATE_PICKER_KEY = "DIALOG_DATE_PICKER_KEY"
    }

    val fajrTime = MutableLiveData(myApp.getString(R.string.loading))
    val dohrTime = MutableLiveData(myApp.getString(R.string.loading))
    val asrTime = MutableLiveData(myApp.getString(R.string.loading))
    val maghrepTime = MutableLiveData(myApp.getString(R.string.loading))
    val eshaTime = MutableLiveData(myApp.getString(R.string.loading))

    fun updateSalawatTimes(salawatTimes: SalawatTimes) {
        fajrTime.value = salawatTimes.getFajrSalahTimeFormat12(myApp)
        dohrTime.value = salawatTimes.getDohrSalahTimeFormat12(myApp)
        asrTime.value = salawatTimes.getAsrSalahTimeFormat12(myApp)
        maghrepTime.value = salawatTimes.getMaghrepSalahTimeFormat12(myApp)
        eshaTime.value = salawatTimes.getEshaSalahTimeFormat12(myApp)
    }

    val cacheOfDateAndSalawatTimes = mutableMapOf<SimpleDate, SalawatTimes>()

    var salahFardType: SalahFardType = SalahFardType.FAGR
        private set

    private val locationData = prefsApp.getLocationData().map { it!! }.asLiveData()

    val address = locationData.map {
        it.address
    }

    val currentDateTime = MutableLiveData(LocalDateTime.now())

    val day = currentDateTime.map {
        myApp.getDayOfWeekName(it.dayOfWeek.value.dec())
    }

    // 27 / 12 / 2021
    val date = currentDateTime.map {
        it.format(DateTimeFormatter.ofPattern("d / M / yyyy", Locale("ar")))
    }

    val canRingFajr = prefsSalah.getCanRingFajr().asLiveData()
    val canRingDohr = prefsSalah.getCanRingDohr().asLiveData()
    val canRingAsr = prefsSalah.getCanRingAsr().asLiveData()
    val canRingMaghrep = prefsSalah.getCanRingMaghrep().asLiveData()
    val canRingEsha = prefsSalah.getCanRingEsha().asLiveData()

    fun showNavDrawer(view: View) {
        view.openDrawerLayout()
    }

    fun editLocation(view: View) {
        view.findNavControllerOfProject().navigate(BottomNavFragmentDirections.actionDestBottomNavToDestEditLocation())
    }

    fun prevDay() {
        currentDateTime.value = currentDateTime.value!!.minusDays(1)
    }

    fun nextDay() {
        currentDateTime.value = currentDateTime.value!!.plusDays(1)
    }

    fun pickDay(view: View) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setTitleText(view.context.getString(R.string.determine_the_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { millis ->
            currentDateTime.value = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
        }

        datePicker.show(view.findFragment<Fragment>().childFragmentManager, DIALOG_DATE_PICKER_KEY)
    }

    fun toSheikhList(view: View, type: SalahFardType) {
        view.findNavControllerOfProject().navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestSheikhList(type)
        )
    }

    fun toggleMuteState(view: View, type: SalahFardType) {
        val canRing = when (type) {
            SalahFardType.FAGR -> canRingFajr.value
            SalahFardType.DOHR -> canRingDohr.value
            SalahFardType.ASR -> canRingAsr.value
            SalahFardType.MAGHREP -> canRingMaghrep.value
            SalahFardType.ESHA -> canRingEsha.value
        } ?: false

        if (canRing) {
            // cancel alarm
            SalawatAlarmsBroadcastReceiver.cancelAlarmManagerAndWorkManager(
                view.context.applicationContext,
                type
            )

            // Toggle local
            viewModelScope.launch {
                when (type) {
                    SalahFardType.FAGR -> prefsSalah.setCanRingFajr(!canRing)
                    SalahFardType.DOHR -> prefsSalah.setCanRingDohr(!canRing)
                    SalahFardType.ASR -> prefsSalah.setCanRingAsr(!canRing)
                    SalahFardType.MAGHREP -> prefsSalah.setCanRingMaghrep(!canRing)
                    SalahFardType.ESHA -> prefsSalah.setCanRingEsha(!canRing)
                }
            }
        }else {
            // schedule alarm
            val idOfDownloadManager = runBlocking {
                prefsSalah.getSalawatNotificationDownloadManagerId(salahFardType).first()
            }

            var salawatTimes = cacheOfDateAndSalawatTimes[LocalDate.now().toSimpleDate()]!!
            var timeInDay = when (type) {
                SalahFardType.FAGR -> salawatTimes.fajrTimeInDay
                SalahFardType.DOHR -> salawatTimes.dohrTimeInDay
                SalahFardType.ASR -> salawatTimes.asrTimeInDay
                SalahFardType.MAGHREP -> salawatTimes.maghrepTimeInDay
                SalahFardType.ESHA -> salawatTimes.eshaTimeInDay
            }
            val now = LocalDateTime.now()
            var localDateTime = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(timeInDay.hour24, timeInDay.minute)
            )

            Timber.e("localDateTime < now -> ${localDateTime < now}")
            if (localDateTime < now) {
                val tomorrowSalawatTimes = cacheOfDateAndSalawatTimes[LocalDate.now().plusDays(1).toSimpleDate()]

                if (tomorrowSalawatTimes != null) {
                    salawatTimes = tomorrowSalawatTimes

                    timeInDay = when (type) {
                        SalahFardType.FAGR -> salawatTimes.fajrTimeInDay
                        SalahFardType.DOHR -> salawatTimes.dohrTimeInDay
                        SalahFardType.ASR -> salawatTimes.asrTimeInDay
                        SalahFardType.MAGHREP -> salawatTimes.maghrepTimeInDay
                        SalahFardType.ESHA -> salawatTimes.eshaTimeInDay
                    }

                    localDateTime = LocalDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(timeInDay.hour24, timeInDay.minute)
                    )
                }else {
                    val localDate = LocalDate.now().plusDays(1)

                    view.findFragment<SalahFragment>().executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                        afterShowingLoading = {
                            repoAzan.getAzanTimesSuspend(localDate)
                        },
                        afterHidingLoading = { response ->
                            if (response == null) {
                                view.context.showErrorToast(view.context.getString(R.string.something_went_wrong))

                                return@executeOnGlobalLoadingAndAutoHandleRetryCancellable
                            }

                            cacheOfDateAndSalawatTimes[localDate.toSimpleDate()] = response.timings

                            salawatTimes = response.timings

                            timeInDay = when (type) {
                                SalahFardType.FAGR -> salawatTimes.fajrTimeInDay
                                SalahFardType.DOHR -> salawatTimes.dohrTimeInDay
                                SalahFardType.ASR -> salawatTimes.asrTimeInDay
                                SalahFardType.MAGHREP -> salawatTimes.maghrepTimeInDay
                                SalahFardType.ESHA -> salawatTimes.eshaTimeInDay
                            }

                            localDateTime = LocalDateTime.of(
                                LocalDate.now().plusDays(1),
                                LocalTime.of(timeInDay.hour24, timeInDay.minute)
                            )

                            SalawatAlarmsBroadcastReceiver.scheduleAlarmManagerAndWorkManager(
                                view.context.applicationContext,
                                localDateTime,
                                type,
                                idOfDownloadManager
                            )

                            // Toggle local
                            viewModelScope.launch {
                                when (type) {
                                    SalahFardType.FAGR -> prefsSalah.setCanRingFajr(!canRing)
                                    SalahFardType.DOHR -> prefsSalah.setCanRingDohr(!canRing)
                                    SalahFardType.ASR -> prefsSalah.setCanRingAsr(!canRing)
                                    SalahFardType.MAGHREP -> prefsSalah.setCanRingMaghrep(!canRing)
                                    SalahFardType.ESHA -> prefsSalah.setCanRingEsha(!canRing)
                                }
                            }
                        },
                        canCancelDialog = true
                    )

                    return
                }
            }

            SalawatAlarmsBroadcastReceiver.scheduleAlarmManagerAndWorkManager(
                view.context.applicationContext,
                localDateTime,
                type,
                idOfDownloadManager
            )

            // Toggle local
            viewModelScope.launch {
                when (type) {
                    SalahFardType.FAGR -> prefsSalah.setCanRingFajr(!canRing)
                    SalahFardType.DOHR -> prefsSalah.setCanRingDohr(!canRing)
                    SalahFardType.ASR -> prefsSalah.setCanRingAsr(!canRing)
                    SalahFardType.MAGHREP -> prefsSalah.setCanRingMaghrep(!canRing)
                    SalahFardType.ESHA -> prefsSalah.setCanRingEsha(!canRing)
                }
            }
        }
    }

}

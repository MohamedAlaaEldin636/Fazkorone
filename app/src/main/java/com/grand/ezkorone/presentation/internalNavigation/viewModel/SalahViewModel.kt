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
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.myApp
import com.grand.ezkorone.core.extensions.openDrawerLayout
import com.grand.ezkorone.data.azan.repository.RepositoryAzan
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.local.preferences.PrefsSalah
import com.grand.ezkorone.domain.azan.SalawatTimes
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.domain.salah.SimpleDate
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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
        fajrTime.value = salawatTimes.getFajrSalahTimeFormat(myApp)
        dohrTime.value = salawatTimes.getDohrSalahTimeFormat(myApp)
        asrTime.value = salawatTimes.getAsrSalahTimeFormat(myApp)
        maghrepTime.value = salawatTimes.getMaghrepSalahTimeFormat(myApp)
        eshaTime.value = salawatTimes.getEshaSalahTimeFormat(myApp)
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
        it.format(DateTimeFormatter.ofPattern("cccc", Locale("ar")))
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
        // todo default all muted as osama said and from local prefs saved locally isa,
        //  ALSO on toggle to be not mute immediately launch worker manager and alarm manager
        //  to set alarms isa, also get mawa3ed el salaha mn el save elle kan f el home isa.
        /*
        set alarm manager with periodic on same time and launch work manager once per day from 12 to 3 msln to adjust
        new time isa, and make work manager with constraint of wifi or data isa,
        hmmmm m4 mmkn tt3eref in future maybe launch from 10 to 3 ahe el window tb2a akbar 34an el net isa.
         */
    }

}

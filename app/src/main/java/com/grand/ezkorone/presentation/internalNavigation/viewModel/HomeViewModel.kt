package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.chrono.Chronology
import java.time.chrono.HijrahChronology
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    val address = MutableLiveData("")

    val regularDate = MutableLiveData("")

    val hijrahDate = MutableLiveData("")

    val search = MutableLiveData("")

    val prayExactTime = MutableLiveData("")
    val prayRemainingTime = MutableLiveData("")

    fun showNavDrawer(view: View) {
        view.findNavControllerOfProject()
        //HijrahChronology
        // todo
    }

    fun toSearchPlace(view: View) {
        // todo
    }

    fun editLocation(view: View) {
        // todo
    }

    fun toPrayBottomNav(view: View) {
        // todo
    }

}
package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.domain.salah.SalahFardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalahViewModel @Inject constructor(

) : ViewModel() {

    val address = MutableLiveData("")

    fun showNavDrawer(view: View) {
        view.findNavControllerOfProject()
        //HijrahChronology
        // todo
    }

    fun editLocation(view: View) {
        // todo
    }

    fun prevDay(view: View) {
        // todo
    }

    fun nextDay(view: View) {
        // todo
    }

    fun pickDay(view: View) {
        // todo show date picker see samee kda isa in create order isa.
    }

    fun toSheikhList(view: View, type: SalahFardType) {
        // todo
    }

    fun toggleMuteState(view: View, type: SalahFardType) {
        // todo
    }

}

package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaspehViewModel @Inject constructor(

) : ViewModel() {

    private val _count = MutableLiveData(0)
    val count = _count.map { it.toString() }

    fun reset(view: View) {
        // todo
    }
    fun decrement(view: View) {
        // todo
    }
    fun increment(view: View) {
        // todo
    }

    fun showNavDrawer(view: View) {
        view.findNavControllerOfProject()
        //HijrahChronology
        // todo
    }

    fun editLocation(view: View) {
        // todo
    }

    fun download(view: View) {
        // todo
    }

    fun prevZekr(view: View) {
        // todo
    }

    fun nextZekr(view: View) {
        // todo
    }

    fun pickAnotherZekr(view: View) {
        // todo
    }

    fun sheikh(view: View) {
        // todo
    }

    fun play(view: View) {
        // todo
    }

    fun share(view: View) {
        // todo
    }

}

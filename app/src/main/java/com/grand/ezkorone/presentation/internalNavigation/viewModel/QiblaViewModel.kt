package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QiblaViewModel @Inject constructor(

) : ViewModel() {

    fun showNavDrawer(view: View) {
        view.findNavControllerOfProject()
        //HijrahChronology
        // todo
    }

    fun editLocation(view: View) {
        // todo
    }

}

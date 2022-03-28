package com.grand.ezkorone.presentation.onBoard.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.grand.ezkorone.presentation.location.LocationSelectionFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(

) : ViewModel() {

    fun enableReceivingNotifications(view: View) {
        // todo save in prefs the enable/disable + call api to tell backend as well isa, then navigation to next destination isa.
    }

    fun disableReceivingNotifications(view: View) {
        // todo see above todo isa.
    }

}

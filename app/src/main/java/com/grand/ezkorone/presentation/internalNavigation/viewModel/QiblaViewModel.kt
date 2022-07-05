package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.location.Location
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.core.customTypes.map
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.getAddressFromLatitudeAndLongitude
import com.grand.ezkorone.core.extensions.openDrawerLayout
import com.grand.ezkorone.presentation.location.EditLocationFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QiblaViewModel @Inject constructor(

) : ViewModel() {

    var myCurrentLocation: Location? = null
    var kaabaLocation: Location? = null

    val featureIsSupported = MutableLiveData(true)

    val currentDegrees = MutableLiveData(0)
    val currentDegreesText = currentDegrees.map { "$it°" } /**/
    val showAccuracyCalibration = MutableLiveData(false)

    val forceHideShowAccuracyCalibration = MutableLiveData(false)
    val accuracyIsLowNotMedium = MutableLiveData(false)

    fun showNavDrawer(view: View) {
        view.openDrawerLayout()
    }

    fun performForceHideShowAccuracyCalibration() {
        forceHideShowAccuracyCalibration.value = true
    }

}

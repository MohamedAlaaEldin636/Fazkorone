package com.grand.ezkorone.presentation.location.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.presentation.location.EditLocationFragment
import com.grand.ezkorone.presentation.location.LocationSelectionFragment
import com.grand.ezkorone.presentation.location.LocationSelectionFragmentArgs
import com.grand.ezkorone.presentation.location.LocationSelectionFragmentDirections
import com.grand.ezkorone.presentation.location.adapter.RVItemText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditLocationViewModel @Inject constructor(
    private val prefsApp: PrefsApp,
    private val gson: Gson,
) : ViewModel() {

    private val locationData = prefsApp.getLocationData().map { it!! }.asLiveData()

    val address = locationData.map {
        it.address
    }

    val showLottie = MutableLiveData(false)

    var myCurrentLocation: LatLng? = null

    val addresses = prefsApp.getLocationsList()

    val adapter = RVItemText(gson) { view, locationData ->
        onSelectLocationClick(view, locationData.latitude, locationData.longitude, locationData.address)
    }

    val showEmptyView = MutableLiveData(false)

    fun autoDetectLocation(view: View) {
        view.findFragment<EditLocationFragment>().checkIfPermissionsGrantedToMoveOrRequestThem()
    }

    fun toSearchPlace(view: View) {
        val latitude = locationData.value?.latitude ?: return
        val longitude = locationData.value?.longitude ?: return

        view.findNavController().navigateDeepLinkWithoutOptions(
            "fragment-dest",
            "com.maproductions.mohamedalaa.shared.location.selection",
            latitude,
            longitude,
        )
    }

    fun onSelectLocationClick(view: View, latitude: String, longitude: String, possibleAddress: String? = null) {
        Timber.e("special location -> $latitude ==== $longitude")

        val fragment = view.findFragment<EditLocationFragment>()

        fragment.activityViewModel.globalLoading.value = true

        fragment.lifecycleScope.launch {
            val address = possibleAddress ?: fragment.requireContext().getAddressFromLatitudeAndLongitude(
                latitude.toDouble(),
                longitude.toDouble()
            )

            val locationData = LocationData(
                latitude,
                longitude,
                address
            )

            prefsApp.setLocationData(locationData)

            fragment.activityViewModel.globalLoading.value = false
        }
    }

}
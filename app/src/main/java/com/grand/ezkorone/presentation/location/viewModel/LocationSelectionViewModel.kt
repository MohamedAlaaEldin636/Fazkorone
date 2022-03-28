package com.grand.ezkorone.presentation.location.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.core.extensions.getAddressFromLatitudeAndLongitude
import com.grand.ezkorone.core.extensions.navigateDeepLinkWithOptions
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.core.extensions.toJson
import com.grand.ezkorone.presentation.location.LocationSelectionFragment
import com.grand.ezkorone.presentation.location.LocationSelectionFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LocationSelectionViewModel @Inject constructor(
    val arg: LocationSelectionFragmentArgs,
    private val gson: Gson,
) : ViewModel() {

    /** Used for [LocationSelectionFragmentArgs.onBoardLocationSelection] */
    val skipAble = arg.onBoardLocationSelection

    val search = MutableLiveData("")

    val showMapNotSearchResults = MutableLiveData(true)

    var myCurrentLocation: LatLng? = null

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

    fun toSearchPlace(view: View) {
        showMapNotSearchResults.value = false

        val fragment = view.findFragment<LocationSelectionFragment>()

        if (!Places.isInitialized()) {
            Places.initialize(view.context.applicationContext, view.context.getString(R.string.google_geo_api_key))
        }

        val autocompleteSupportFragment = (fragment.childFragmentManager
            .findFragmentById(R.id.placesFragmentContainerView) as AutocompleteSupportFragment)

        autocompleteSupportFragment.setPlaceFields(listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG
        ))

        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLng = LatLng(place.latLng?.latitude ?: 0.0, place.latLng?.longitude ?: 0.0)

                fragment.googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, fragment.zoom))

                showMapNotSearchResults.postValue(true)
            }

            override fun onError(status: Status) {
                Timber.e("Places API error with status $status")

                fragment.requireContext().showErrorToast(fragment.getString(R.string.something_went_wrong))

                showMapNotSearchResults.postValue(true)
            }
        })
    }

    fun moveToCurrentLocation(view: View) {
        view.findFragment<LocationSelectionFragment>().checkIfPermissionsGrantedToMoveOrRequestThem()
    }

    fun onSelectLocationClick(view: View) {
        val fragment = view.findFragment<LocationSelectionFragment>()

        val googleMap = fragment.googleMap ?: return

        fragment.activityViewModel.globalLoading.value = true

        fragment.lifecycleScope.launch {
            val address = fragment.requireContext().getAddressFromLatitudeAndLongitude(
                googleMap.cameraPosition.target.latitude,
                googleMap.cameraPosition.target.longitude
            )

            val locationData = LocationData(
                googleMap.cameraPosition.target.latitude.toString(),
                googleMap.cameraPosition.target.longitude.toString(),
                address
            )

            fragment.activityViewModel.globalLoading.value = false

            delay(300)

            val navController = fragment.findNavController()

            navController.navigateUp()

            navController.currentBackStackEntry?.savedStateHandle?.set(
                LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
                locationData.toJson(gson)
            )
        }
    }

    fun skip(view: View) {
        val fragment = view.findFragment<LocationSelectionFragment>()

        val navController = fragment.findNavController()

        // todo make a special deep link navigation isa.
        navController.navigateDeepLinkWithOptions(TODO(), TODO())
    }

}

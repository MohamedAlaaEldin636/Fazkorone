package com.grand.ezkorone.presentation.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.grand.ezkorone.core.extensions.showNormalToast
import com.grand.ezkorone.databinding.FragmentLocationSelectionBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.location.viewModel.LocationSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.showErrorToast

@AndroidEntryPoint
class LocationSelectionFragment : MABaseFragment<FragmentLocationSelectionBinding>(),
    OnMapReadyCallback {

    companion object {
        const val KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON = "KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON"
    }

    private val viewModel by viewModels<LocationSelectionViewModel>()

    var googleMap: GoogleMap? = null
        private set

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val permissionLocationRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                || permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                moveToCurrentLocation()
            }
            else -> {
                requireContext().showNormalToast(getString(R.string.you_didn_t_accept_permission))
            }
        }
    }

    /** Zoom levels https://developers.google.com/maps/documentation/android-sdk/views#zoom */
    val zoom get() = min(googleMap?.maxZoomLevel ?: 5f, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun getLayoutId(): Int = R.layout.fragment_location_selection

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup map
        (childFragmentManager.findFragmentById(R.id.mapFragmentContainerView) as? SupportMapFragment)
            ?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        if (viewModel.arg.latitude == null || viewModel.arg.longitude == null) {
            checkIfPermissionsGrantedToMoveOrRequestThem()
        }

        val location = LatLng(
            viewModel.arg.latitude?.toDouble() ?: 0.0,
            viewModel.arg.longitude?.toDouble() ?: 0.0
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }

    @SuppressLint("MissingPermission")
    fun checkIfPermissionsGrantedToMoveOrRequestThem() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            moveToCurrentLocation()
        }else {
            permissionLocationRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    private fun moveToCurrentLocation() {
        viewModel.myCurrentLocation?.also { myCurrentLocation ->
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myCurrentLocation, zoom))

            return
        }

        activityViewModel.globalLoading.value = true

        val cancellationToken = object : CancellationToken() {
            override fun onCanceledRequested(listener: OnTokenCanceledListener): CancellationToken = this

            override fun isCancellationRequested(): Boolean = false
        }

        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationToken
        ).addOnSuccessListener { location: Location? ->
            if (location == null) {
                activityViewModel.globalLoading.postValue(false)

                requireContext().showErrorToast(getString(R.string.check_location_turned_on))

                return@addOnSuccessListener
            }

            val latLng = LatLng(location.latitude, location.longitude)

            viewModel.myCurrentLocation = latLng

            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

            activityViewModel.globalLoading.postValue(false)

            // to-do Might be null in case location settings not turned on, or other reasons check links
            // please turn on location in settings + use gecoder https://developers.google.com/maps/documentation/geocoding/overview
            // for address which requires google api key
            // for searching like hot information search even more
            // for now leave this as user clicked skip for example.
            // search requires same google_map api key from Places as it launches new activity
        }
    }

}

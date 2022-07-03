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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.grand.ezkorone.core.customTypes.LocationHandler
import com.grand.ezkorone.core.extensions.showErrorToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LocationSelectionFragment : MABaseFragment<FragmentLocationSelectionBinding>(),
    OnMapReadyCallback, LocationHandler.Listener {

    companion object {
        const val KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON = "KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON"
    }

    private val viewModel by viewModels<LocationSelectionViewModel>()

    /** Zoom levels https://developers.google.com/maps/documentation/android-sdk/views#zoom */
    val zoom get() = min(viewModel.googleMap?.maxZoomLevel ?: 5f, 15f)

    private lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            requireContext(),
            this
        )

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_location_selection

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.e("onViewCreated googleMap == null ${viewModel.googleMap == null}")
        // Setup map
        (childFragmentManager.findFragmentById(R.id.mapFragmentContainerView) as? SupportMapFragment)
            ?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.googleMap = googleMap

        if (viewModel.arg.latitude == null || viewModel.arg.longitude == null) {
            if (!viewModel.arg.onBoardLocationSelection) {
                moveToCurrentLocation()
            }
        }

        val location = LatLng(
            viewModel.arg.latitude?.toDouble() ?: 0.0,
            viewModel.arg.longitude?.toDouble() ?: 0.0
        )

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                viewModel.resultOfPlaceFragment.value ?: location,
                zoom
            )
        )
    }

    fun moveToCurrentLocation() {
        viewModel.myCurrentLocation?.also { myCurrentLocation ->
            viewModel.googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(myCurrentLocation, zoom)
            )
        } ?: locationHandler.requestCurrentLocation(true)
    }

    override fun onCurrentLocationResultSuccess(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)

        viewModel.myCurrentLocation = latLng

        viewModel.googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

}

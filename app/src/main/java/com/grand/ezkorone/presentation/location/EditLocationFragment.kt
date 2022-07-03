package com.grand.ezkorone.presentation.location

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.core.customTypes.LocationHandler
import com.grand.ezkorone.core.extensions.fromJson
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.core.extensions.showNormalToast
import com.grand.ezkorone.core.extensions.toJson
import com.grand.ezkorone.databinding.FragmentEditLocationBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.location.viewModel.EditLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditLocationFragment : MABaseFragment<FragmentEditLocationBinding>(), LocationHandler.Listener {

    private val viewModel by viewModels<EditLocationViewModel>()

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

    override fun getLayoutId(): Int = R.layout.fragment_edit_location

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.adapter = viewModel.adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addresses.collectLatest {
                    viewModel.adapter.submitList(it)

                    viewModel.showEmptyView.value = it.isEmpty()
                }
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            ""
        )?.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                findNavController().currentBackStackEntry?.savedStateHandle?.set(
                    LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
                    ""
                )

                val locationData = it.fromJson<LocationData>()

                binding?.root?.also { view ->
                    viewModel.onSelectLocationClick(
                        view,
                        locationData.latitude,
                        locationData.longitude,
                        locationData.address,
                    )
                } ?: context?.showErrorToast(getString(R.string.something_went_wrong))
            }
        }
    }

    fun setLocationToCurrentLocation() {
        val myCurrentLocation = viewModel.myCurrentLocation

        if (myCurrentLocation != null) {
            viewModel.onSelectLocationClick(
                binding?.root ?: return,
                myCurrentLocation.latitude.toString(),
                myCurrentLocation.longitude.toString(),
            )
        }else {
            viewModel.showLottie.value = true
            binding?.lottieAnimationView?.playAnimation()

            locationHandler.requestCurrentLocation(true)
        }
    }

    override fun onCurrentLocationResultSuccess(location: Location) {
        viewModel.showLottie.value = false
        binding?.lottieAnimationView?.cancelAnimation()

        viewModel.myCurrentLocation = LatLng(location.latitude, location.longitude)

        binding?.root?.also {
            viewModel.onSelectLocationClick(
                it,
                location.latitude.toString(),
                location.longitude.toString(),
            )
        }
    }

}

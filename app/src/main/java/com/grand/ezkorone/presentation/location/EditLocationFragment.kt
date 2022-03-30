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
class EditLocationFragment : MABaseFragment<FragmentEditLocationBinding>() {

    private val viewModel by viewModels<EditLocationViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val permissionLocationRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                || permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                setLocationToCurrentLocation()
            }
            else -> {
                requireContext().showNormalToast(getString(R.string.you_didn_t_accept_permission))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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

    @SuppressLint("MissingPermission")
    fun checkIfPermissionsGrantedToMoveOrRequestThem() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setLocationToCurrentLocation()
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
    private fun setLocationToCurrentLocation() {
        viewModel.myCurrentLocation?.also { myCurrentLocation ->
            viewModel.onSelectLocationClick(
                binding?.root ?: return,
                myCurrentLocation.latitude.toString(),
                myCurrentLocation.longitude.toString(),
            )

            return
        }

        activityViewModel.globalLoading.value = true
        viewModel.showLottie.value = true
        binding?.lottieAnimationView?.playAnimation()

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
                viewModel.showLottie.value = false
                binding?.lottieAnimationView?.cancelAnimation()

                requireContext().showErrorToast(getString(R.string.check_location_turned_on))

                return@addOnSuccessListener
            }

            viewModel.myCurrentLocation = LatLng(location.latitude, location.longitude)

            binding?.root?.also {
                viewModel.onSelectLocationClick(
                    it,
                    location.latitude.toString(),
                    location.longitude.toString(),
                )
            }

            activityViewModel.globalLoading.postValue(false)
            viewModel.showLottie.value = false
            binding?.lottieAnimationView?.cancelAnimation()

            // to-do Might be null in case location settings not turned on, or other reasons check links
            // please turn on location in settings + use gecoder https://developers.google.com/maps/documentation/geocoding/overview
            // for address which requires google api key
            // for searching like hot information search even more
            // for now leave this as user clicked skip for example.
            // search requires same google_map api key from Places as it launches new activity
        }
    }

}

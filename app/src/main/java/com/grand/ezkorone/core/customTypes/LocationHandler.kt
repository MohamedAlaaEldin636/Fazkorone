package com.grand.ezkorone.core.customTypes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.grand.ezkorone.core.extensions.showAlertDialog
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.main.MainActivity
import com.grand.ezkorone.presentation.main.viewModel.MainViewModel
import timber.log.Timber
import java.lang.Exception
import java.lang.ref.WeakReference
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.launchSafely

class LocationHandler private constructor(
    lifecycle: Lifecycle,
    context: Context,
    host: Any,
    listener: Listener,
    mainViewModel: MainViewModel?,
) : DefaultLifecycleObserver {

    constructor(fragment: MABaseFragment<*>, lifecycle: Lifecycle, context: Context, listener: Listener) : this(
        lifecycle, context, fragment, listener, fragment.activityViewModel
    )

    constructor(activity: MainActivity, lifecycle: Lifecycle, context: Context, listener: Listener) : this(
        lifecycle, context, activity, listener, activity.viewModel
    )

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 10_000L
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2L
    }

    private val weakRefLifecycle = WeakReference(lifecycle)
    private val weakRefContext = WeakReference(context)
    private val weakRefHost = WeakReference(host)
    private val weakRefListener = WeakReference(listener)
    private val weakRefMainViewModel = WeakReference(mainViewModel)

    init {
        lifecycle.addObserver(this)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var requestCurrentLocationNotPeriodicLocation = false

    private var showProgressForCurrentLocation = false

    private val activityResultPermissionLocationRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val activity = getActivityOrNull()

        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    || permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                checkGPSForLocation()
            }
            activity != null && (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) -> {
                Timber.e("activity -> 00000")

                weakRefMainViewModel.get()?.globalLoading?.value = false

                activity.showAlertDialog(
                    activity.getString(R.string.allow_location_permission),
                    activity.getString(R.string.this_app_need_allow_location),
                    onDismissListener = {
                        activity.showErrorToast(activity.getString(R.string.you_didn_t_accept_permission))
                    }
                ) {
                    checkOnPermissions(activity)
                }
            }
            else -> {
                Timber.e("activity -> ${activity != null}")

                if (weakRefMainViewModel.get()?.globalLoading?.value == true) {
                    weakRefMainViewModel.get()?.globalLoading?.value = false
                }

                weakRefListener.get()?.onDenyLocationPermissions(this, weakRefContext.get())
            }
        }
    }

    private val activityResultLocationSystemSettings = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        Timber.e("it?.resultCode == Activity.RESULT_OK ${it?.resultCode == Activity.RESULT_OK}")

        if (it?.resultCode == Activity.RESULT_OK) {
            onGPSSuccess()
        }else {
            onGPSFailure(null)
        }
    }

    private val activityResultPermissionsSystemSettings = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        checkOnPermissions(weakRefContext.get() ?: return@registerForActivityResult)
    }

    override fun onCreate(owner: LifecycleOwner) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(
            weakRefContext.get() ?: return
        )

        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                weakRefListener.get()?.onChangeLocationSuccess(locationResult.lastLocation)
            }
        }
    }

    fun requestCurrentLocation(showProgress: Boolean) {
        val context = weakRefContext.get() ?: return

        if (showProgress) {
            weakRefMainViewModel.get()?.globalLoading?.value = true

            this.showProgressForCurrentLocation = true
        }

        requestCurrentLocationNotPeriodicLocation = true

        checkOnPermissions(context)
    }

    fun requestLocationUpdates() {
        val context = weakRefContext.get() ?: return

        requestCurrentLocationNotPeriodicLocation = false

        checkOnPermissions(context)
    }

    fun stopLocationUpdates() {
        kotlin.runCatching {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun checkOnPermissions(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkGPSForLocation()
        }else {
            activityResultPermissionLocationRequest.launchSafely(
                weakRefContext.get(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private fun onRequestCurrentLocationFailure(exception: Exception?) {
        onRequestCurrentLocationCompleted(null)

        weakRefListener.get()?.onCurrentLocationResultFailure(weakRefContext.get(), exception)
    }

    /** @param location not-null in case of success isa. */
    private fun onRequestCurrentLocationCompleted(location: Location?) {
        if (showProgressForCurrentLocation) {
            weakRefMainViewModel.get()?.globalLoading?.value = false

            showProgressForCurrentLocation = false
        }

        if (location != null) {
            weakRefListener.get()?.onCurrentLocationResultSuccess(location)
        }
    }

    private fun onGPSFailure(exception: Exception?) {
        if (requestCurrentLocationNotPeriodicLocation) {
            onRequestCurrentLocationFailure(exception)
        }else {
            weakRefListener.get()?.onChangeLocationFailure(weakRefContext.get(), exception)
        }
    }

    @SuppressLint("MissingPermission")
    private fun onGPSSuccess() {
        if (requestCurrentLocationNotPeriodicLocation) {
            val cancellationToken = object : CancellationToken() {
                override fun onCanceledRequested(listener: OnTokenCanceledListener): CancellationToken = this

                override fun isCancellationRequested(): Boolean = false
            }

            kotlin.runCatching {
                fusedLocationClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    cancellationToken
                ).addOnSuccessListener currentLocationAddOnSuccessListener@ { location: Location? ->
                    if (location == null) {
                        onRequestCurrentLocationFailure(null)

                        return@currentLocationAddOnSuccessListener
                    }

                    onRequestCurrentLocationCompleted(location)
                }.addOnFailureListener {
                    onRequestCurrentLocationFailure(it)
                }
            }
        }else {
            kotlin.runCatching {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper() ?: Looper.getMainLooper()
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkGPSForLocation() {
        val context = weakRefContext.get() ?: return

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(context)

        client.checkLocationSettings(builder.build()).addOnSuccessListener {
            onGPSSuccess()
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    //exception.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                    activityResultLocationSystemSettings.launchSafely(
                        weakRefContext.get(),
                        IntentSenderRequest.Builder(exception.resolution.intentSender).build()
                    )
                }catch (sendEx: IntentSender.SendIntentException) {
                    onGPSFailure(sendEx)
                }
            }else {
                onGPSFailure(exception)
            }
        }
    }

    private fun shouldShowRequestPermissionRationale(permission: String): Boolean = when (val host = weakRefHost.get()) {
        is Fragment -> host.shouldShowRequestPermissionRationale(permission)
        is AppCompatActivity -> host.shouldShowRequestPermissionRationale(permission)
        else -> false
    }

    private fun getActivityOrNull() = when (val value = weakRefHost.get()) {
        is Fragment -> value.activity
        is AppCompatActivity -> value
        else -> null
    }

    private fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return when (val host = weakRefHost.get()) {
            is Fragment -> {
                host.registerForActivityResult(contract, callback)
            }
            is AppCompatActivity -> {
                host.registerForActivityResult(contract, callback)
            }
            else -> throw RuntimeException("Unexpected host $host")
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        weakRefLifecycle.get()?.removeObserver(this)
    }

    interface Listener {

        fun onCurrentLocationResultSuccess(location: Location) {}

        fun onCurrentLocationResultFailure(context: Context?, exception: Exception?) {
            context?.showErrorToast(context.getString(R.string.something_went_wrong))

            Timber.e("error in get CURRENT location $exception")
        }

        fun onChangeLocationSuccess(location: Location) {}

        fun onChangeLocationFailure(context: Context?, exception: Exception?) {
            context?.showErrorToast(context.getString(R.string.something_went_wrong))

            Timber.e("error in get PERIODIC location $exception")
        }

        fun onDenyLocationPermissions(locationHandler: LocationHandler, context: Context?) {
            locationHandler.getActivityOrNull()?.apply {
                showAlertDialog(
                    getString(R.string.change_permission_in_settings_of_device),
                    getString(R.string.this_app_need_allow_location),
                    onDismissListener = {
                        context?.showErrorToast(context.getString(R.string.you_didn_t_accept_permission))
                    }
                ) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                        it.data = Uri.fromParts("package", packageName, null)
                    }

                    locationHandler.activityResultPermissionsSystemSettings.launchSafely(
                        locationHandler.weakRefContext.get(),
                        intent
                    )
                }
            }
        }

    }

}

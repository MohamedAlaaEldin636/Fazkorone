package com.grand.ezkorone.presentation.internalNavigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.core.extensions.showNormalToast
import com.grand.ezkorone.databinding.FragmentQiblaBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.QiblaViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.round

// todo https://stackoverflow.com/a/44182427
@AndroidEntryPoint
class QiblaFragment : MABaseFragment<FragmentQiblaBinding>(), SensorEventListener {

    private val viewModel by viewModels<QiblaViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var currentDegree = 0f
    private var currentDegreeNeedle = 0f

    private var sensorManager: SensorManager? = null

    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var trialSensor: Sensor? = null

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

    override fun getLayoutId(): Int = R.layout.fragment_qibla

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.myCurrentLocation == null || viewModel.kaabaLocation == null) {
            checkIfPermissionsGrantedToMoveOrRequestThem()
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
        viewModel.myCurrentLocation?.also {
            onLocationDetectedSuccessfully()

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

            viewModel.myCurrentLocation = Location(location)
            viewModel.kaabaLocation = Location(location).also {
                it.latitude = 21.422487
                it.longitude = 39.826206
                //it.bearing = 0.0f todo
                //it.altitude = 277.0
            }

            binding?.root?.also {
                onLocationDetectedSuccessfully()
            }

            activityViewModel.globalLoading.postValue(false)

            // to-do Might be null in case location settings not turned on, or other reasons check links
            // please turn on location in settings + use gecoder https://developers.google.com/maps/documentation/geocoding/overview
            // for address which requires google api key
            // for searching like hot information search even more
            // for now leave this as user clicked skip for example.
            // search requires same google_map api key from Places as it launches new activity
        }
    }

    private fun onLocationDetectedSuccessfully() {
        sensorManager = requireContext().getSystemService()

        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        trialSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        if (accelerometer == null || magnetometer == null) {
            viewModel.featureIsSupported.value = false

            return
        }

        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager?.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager?.registerListener(this, trialSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.myCurrentLocation != null && viewModel.kaabaLocation != null) {
            sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            sensorManager?.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
            sensorManager?.registerListener(this, trialSensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager?.unregisterListener(this, accelerometer)
        sensorManager?.unregisterListener(this, magnetometer)
        sensorManager?.unregisterListener(this, trialSensor)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values
        }else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values
        }else if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
            Timber.i("sensor orientation ${event.values.toList()}")

            onOrientationChanged(event.values)
        }

        /*val gravity = this.gravity ?: return
        val geomagnetic = this.geomagnetic ?: return

        val rotation = FloatArray(9)
        val inclination = FloatArray(9)
        val success = SensorManager.getRotationMatrix(rotation, inclination, gravity, geomagnetic)
        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotation, orientation)

            //val azimuth = orientation[0]
            onOrientationChanged(orientation)
        }*/
    }

    private fun onOrientationChanged(orientation: FloatArray) {
        val myCurrentLocation = viewModel.myCurrentLocation ?: return
        val kaabaLocation = viewModel.kaabaLocation ?: return

        val degree = round(orientation[0])
        var head = round(orientation[0])

        var bearTo = myCurrentLocation.bearingTo(kaabaLocation)

        // bearTo = The angle from true north to the destination location from the point we're your
        // currently standing

        // head = The angle that you've rotated your phone from true north.

        val geoField = GeomagneticField(
            myCurrentLocation.latitude.toFloat(),
            myCurrentLocation.longitude.toFloat(),
            myCurrentLocation.altitude.toFloat(),
            System.currentTimeMillis()
        )

        head -= geoField.declination // converts magnetic north into true north

        if (bearTo < 0) {
            bearTo += 360
        }

        // This is where we choose to point it
        var direction = bearTo - head

        // If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction < 0) {
            direction += 360;
        }

        val raQibla = RotateAnimation(
            currentDegreeNeedle, direction,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).also {
            it.duration = 210
            it.fillAfter = true
        }

        binding?.likeNeedleImageView?.startAnimation(raQibla)

        currentDegreeNeedle = direction

        // create a rotation animation (reverse turn degree degrees)
        val ra = RotateAnimation(
            currentDegree, -degree,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f,
        ).also {
            it.duration = 210
            it.fillAfter = true
        }

        binding?.likeCompassImageView?.startAnimation(ra)

        currentDegree = -degree

        Timber.d("orientation ${orientation.toList()}, direction $direction, degree ${-degree}")
    }

}


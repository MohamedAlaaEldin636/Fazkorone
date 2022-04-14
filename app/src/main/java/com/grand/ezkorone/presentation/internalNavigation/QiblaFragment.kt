package com.grand.ezkorone.presentation.internalNavigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.view.Display
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
import kotlin.math.*

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
        sensorManager?.unregisterListener(this, accelerometer)
        sensorManager?.unregisterListener(this, magnetometer)
        sensorManager?.unregisterListener(this, trialSensor)

        super.onPause()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //SensorManager.SENSOR_STATUS_*
        //SensorManager.SENSOR_STATUS_ACCURACY_HIGH
        /*
        يرجى تدوير هاتفك وجعل 8 فى الهواء من اجل الحصول على توجيهات دقيقة للقبلة
         */
        if (sensor?.type == Sensor.TYPE_ORIENTATION) {
            viewModel.showAccuracyCalibration.value = accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW
                || accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM

            val textAccuracy = when (accuracy) {
                SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "High"
                SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Medium"
                SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "Low"
                else -> "Unknown"
            }

            Timber.e("AAAAAAAAAA -> accuracy $textAccuracy")
        }/*else {
            viewModel.showAccuracyCalibration.value = false
        }*/
    }

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values
        }else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values
        }else if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
            Timber.i("sensor orientation ${event.values.toList()}")

            //onOrientationChanged(event.values)

            calcNorth(event.values[0])
        }

        val gravity = this.gravity ?: return
        val geomagnetic = this.geomagnetic ?: return

        val rotation = FloatArray(9)
        val inclination = FloatArray(9)
        val success = SensorManager.getRotationMatrix(rotation, inclination, gravity, geomagnetic)
        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotation, orientation)

            //val azimuth = orientation[0] // Math.toDegrees kda  ?!
            val azimuth1 = orientation[0]
            val azimuth2 = Math.toDegrees(azimuth1.toDouble()).toFloat() // Correct 135 brdo todo oooooooooooooooo
            Timber.e("djosaijdsa $azimuth1 ==== $azimuth2")
            //onOrientationChanged(orientation)
            //calcNorth(azimuth2) // btr3e4 awi fa mate3melsh change unless 4 degres or 3 kda ya3ne isa.
        }
    }

    /*todo e3mel el 2ebla mn el north w zawed text w 2ole kam degree kda isa.*/
    private fun calcNorth(azimut: Float) {
        val degree = -azimut * 360f / (2f * 3.14159f)
        Timber.w("azimut $azimut")
        Timber.w("degree $degree")

        val finalDegrees = -azimut

        binding?.likeCompassImageView?.rotation = finalDegrees

        viewModel.currentDegrees.value = finalDegrees.roundToInt()

        val newDegrees = (finalDegrees.roundToInt() + 128).toFloat()

        binding?.likeNeedleImageView?.rotation = newDegrees

        val showOn = newDegrees in 350f..360f || newDegrees in 0f..10f
        binding?.indicatorImageView?.setImageResource(
            if (showOn) R.drawable.dr_qibla_on else R.drawable.dr_qibla_off
        )
    }

    // todo likeCompassImageView
    private fun trial1(otherDegree: Float) {
        val myCurrentLocation = viewModel.myCurrentLocation ?: return
        val kaabaLocation = viewModel.kaabaLocation ?: return

        val lat1 = myCurrentLocation.latitude
        val lon1 = myCurrentLocation.longitude
        val lat2 = kaabaLocation.latitude
        val lon2 = kaabaLocation.longitude

        val degrees = calcDegrees(lat1, lon1, lat2, lon2)

        val newDegrees = (otherDegree - degrees.toFloat()).let {
            val new1 = if (it >= 0f) it else it.plus(360f)

            val new2 = if (new1 >= 360f) new1.minus(360f) else new1

            val new3 = new2 + 100f // +3.7f

            new2
            /*when {
                new3 >= 360f -> new3.minus(360f)
                new3 < 0f -> new3.plus(360f)
                else -> new3
            }*/
        }

        // 180 360 bs kda isa.
        Timber.v("other Degree -> $otherDegree\ncalc Degree -> $degrees\nnew Degree $newDegrees")

        binding?.likeNeedleImageView?.rotation = newDegrees

        val showOn = newDegrees in 355f..360f || newDegrees in 0f..5f
        binding?.indicatorImageView?.setImageResource(
            if (showOn) R.drawable.dr_qibla_on else R.drawable.dr_qibla_off
        )
    }

    private fun calcDegrees(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        /*val lonDelta = lon2 - lon1
        val y = sin(lonDelta) * cos(lat2)
        val x = ( cos(lat1) * sin(lat2) ) - ( sin(lat1) * cos(lat2) * cos(lonDelta) )
        return Math.toDegrees(atan2(y, x))*/
        val myCurrentLocation = viewModel.myCurrentLocation!!
        val kaabaLocation = viewModel.kaabaLocation!!
        return myCurrentLocation.bearingTo(kaabaLocation).toDouble()
    }

    private fun onOrientationChanged(orientation: FloatArray) {
        if (true) {
            val z = orientation[0]//Math.toDegrees(orientation[0].toDouble()) // 114 ranges from -180 to 180
            val x = orientation[1]//Math.toDegrees(orientation[1].toDouble())
            val y = orientation[2]//Math.toDegrees(orientation[2].toDouble())
            Timber.e("z axis -> $z\nx axis -> $x\ny axis -> $y")
            trial1(orientation[0])
            return
        }

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

        // todo
        if (true) {
            Timber.e("djosaijdsa direction $direction")

            Timber.e("djosaijdsa z axis -> ${Math.toDegrees(direction.toDouble())}\ndirections -> $direction")
            //trial1(orientation[0])
            binding?.likeNeedleImageView?.rotation = direction

            return
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


package com.grand.ezkorone.presentation.internalNavigation

import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.LocationHandler
import com.grand.ezkorone.core.extensions.orZero
import com.grand.ezkorone.databinding.FragmentQiblaBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.QiblaViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class QiblaFragment : MABaseFragment<FragmentQiblaBinding>(), SensorEventListener, LocationHandler.Listener {

    private val viewModel by viewModels<QiblaViewModel>()

    private var sensorManager: SensorManager? = null

    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

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

    override fun getLayoutId(): Int = R.layout.fragment_qibla

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.myCurrentLocation == null || viewModel.kaabaLocation == null) {
            setLocationToCurrentLocation()
        }
    }

    private fun setLocationToCurrentLocation() {
        viewModel.myCurrentLocation?.also {
            onLocationDetectedSuccessfully()
        } ?: locationHandler.requestCurrentLocation(true)
    }

    override fun onCurrentLocationResultSuccess(location: Location) {
        viewModel.myCurrentLocation = Location(location)
        viewModel.kaabaLocation = Location(location).also {
            it.latitude = 21.422487
            it.longitude = 39.826206
            it.bearing = 0.0f
            it.altitude = 277.0
        }

        binding?.root?.also {
            onLocationDetectedSuccessfully()
        }
    }

    private fun onLocationDetectedSuccessfully() {
        sensorManager = requireContext().getSystemService()

        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (accelerometer == null || magnetometer == null) {
            viewModel.featureIsSupported.value = false

            return
        }

        sensorManager?.registerListener(this, accelerometer, delayInMicroseconds)
        sensorManager?.registerListener(this, magnetometer, delayInMicroseconds)
    }

    private val delayInMicroseconds = SensorManager.SENSOR_DELAY_UI

    override fun onResume() {
        super.onResume()

        if (viewModel.myCurrentLocation != null && viewModel.kaabaLocation != null) {
            sensorManager?.registerListener(this, accelerometer, delayInMicroseconds)
            sensorManager?.registerListener(this, magnetometer, delayInMicroseconds)
        }
    }

    override fun onPause() {
        sensorManager?.unregisterListener(this, accelerometer)
        sensorManager?.unregisterListener(this, magnetometer)

        super.onPause()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (sensor?.type == Sensor.TYPE_ACCELEROMETER || sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            viewModel.showAccuracyCalibration.value = accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW
                || accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM

            val textAccuracy = when (accuracy) {
                SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "High"
                SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Medium"
                SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "Low"
                else -> "Unknown"
            }

            Timber.e("AAAAAAAAAA -> accuracy $textAccuracy")
        }
    }

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null
    //private var rotateAnimation: RotateAnimation? = null

    /*var job: Job? = null
    fun View.performAnimation(
        newRotation: Float,
        durationInMillis: Long = 100_000,
        updateEveryInMillis: Long = 17_000
    ) {
        job?.cancel()
        job = lifecycleScope.launch {
            var dur = durationInMillis
            val percent = ((updateEveryInMillis.toDouble() / durationInMillis.toDouble()) * 100.0).toFloat()
            val amount = ((rotation - newRotation).absoluteValue) * percent
            while (true) {
                if (dur <= updateEveryInMillis) {
                    rotation = newRotation
                    break
                }
                dur -= updateEveryInMillis
                if (rotation < newRotation) {
                    rotation += amount
                }else {
                    rotation -= amount
                }
                delay(updateEveryInMillis)
            }
        }
    }*/

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values
        }else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values
        }/*else if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
            Timber.i("sensor orientation ${event.values.toList()}")

            //onOrientationChanged(event.values)

            calcNorth(event.values[0])
        }*/

        val gravity = this.gravity ?: return
        val geomagnetic = this.geomagnetic ?: return
        val myLocation = viewModel.myCurrentLocation ?: return
        val kaabaLocation = viewModel.kaabaLocation ?: return

        val rotation = FloatArray(9)
        val inclination = FloatArray(9)
        val success = SensorManager.getRotationMatrix(rotation, inclination, gravity, geomagnetic)
        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotation, orientation)
            //SensorManager.getInclination(inclination)

            val azimuthInRadians = orientation[0]
            var azimuthInDegree = Math.toDegrees(azimuthInRadians.toDouble())

            Timber.e("azimuthInDegree $azimuthInDegree\n$azimuthInRadians")

            val geomagneticField = GeomagneticField(
                myLocation.latitude.toFloat(),
                myLocation.longitude.toFloat(),
                myLocation.altitude.toFloat(),
                System.currentTimeMillis()
            )

            Timber.e("declination ${geomagneticField.declination}")

            azimuthInDegree -= geomagneticField.declination.toDouble()

            Timber.e("Second azimuthInDegree $azimuthInDegree")

            val bearing = myLocation.bearingTo(kaabaLocation).let {
                if (it < 0) it + 360 else it
            }

            Timber.e("bearing $bearing")

            val direction = (bearing - azimuthInDegree).let {
                if (it < 0) it + 360 else it
            }
            Timber.e("direction $direction")

            val range = direction.toFloat().let {
                it.minus(4)..it.plus(4)
            }

            Timber.e("range ${binding?.likeNeedleImageView?.rotation.orZero()} $range")
            val firstTime = binding?.likeNeedleImageView?.rotation == binding?.likeCompassImageView?.rotation
            if (
                firstTime ||
                !(binding?.likeNeedleImageView?.rotation.orZero() in range ||
                binding?.likeNeedleImageView?.rotation.orZero().plus(360) in range)
            ) {
                Timber.e("diffff ${binding?.likeNeedleImageView?.rotation.orZero() - direction.toFloat()}")

                binding?.likeNeedleImageView?.rotation = direction.toFloat()

                binding?.likeCompassImageView?.rotation = direction.toFloat() - 137

                val showOn = direction.toFloat() in 350f..360f || direction.toFloat() in 0f..10f
                binding?.indicatorImageView?.setImageResource(
                    if (showOn) R.drawable.dr_qibla_on else R.drawable.dr_qibla_off
                )
            }
        }
    }

}


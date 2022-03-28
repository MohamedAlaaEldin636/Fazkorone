package com.grand.ezkorone.core.extensions

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.annotation.WorkerThread
import com.grand.ezkorone.R
import com.grand.ezkorone.presentation.base.MABaseActivity
import timber.log.Timber
import java.util.*

@WorkerThread
fun Context.getAddressFromLatitudeAndLongitude(
    latitude: Double,
    longitude: Double,
    fallbackAddress: String = getString(R.string.your_address_has_been_selected_successfully),
): String {
    val address = try {
        val geocoder = Geocoder(this, Locale(MABaseActivity.APPLICATION_DEFAULT_LANGUAGE))
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            addresses[0].getAddressLine(0).also {
                Timber.d(it)
            }
        }else {
            Timber.d("address NULL")

            null
        }
    }catch (throwable: Throwable) {
        Timber.e(throwable)

        null
    }

    return address ?: fallbackAddress
}

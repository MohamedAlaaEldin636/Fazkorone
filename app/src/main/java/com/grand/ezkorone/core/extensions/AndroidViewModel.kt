package com.grand.ezkorone.core.extensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel

val AndroidViewModel.myApp get() = getApplication<Application>()

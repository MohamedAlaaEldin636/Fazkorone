package com.grand.ezkorone.presentation.main.viewModel

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import com.grand.ezkorone.core.customTypes.GlobalError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor() : ViewModel() {

    val showToolbar = MutableLiveData(false)

    val titleToolbar = MutableLiveData("")

    val centerToolbar = MutableLiveData(true)

    val globalLoading = MutableLiveData(false)

    val globalError = MutableLiveData<GlobalError>(GlobalError.Cancel)

}

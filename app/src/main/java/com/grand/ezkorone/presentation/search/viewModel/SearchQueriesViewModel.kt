package com.grand.ezkorone.presentation.search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchQueriesViewModel @Inject constructor() : ViewModel() {

    val search = MutableLiveData("")

}

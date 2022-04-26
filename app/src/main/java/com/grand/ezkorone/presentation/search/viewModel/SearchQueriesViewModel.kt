package com.grand.ezkorone.presentation.search.viewModel

import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.google.android.material.textfield.TextInputEditText
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.data.home.repository.RepositoryHome
import com.grand.ezkorone.data.search.repository.RepositorySearch
import com.grand.ezkorone.presentation.search.adapter.RVSearchQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchQueriesViewModel @Inject constructor(
    searchRepo: RepositorySearch
) : ViewModel() {

    val query = MutableLiveData("")

    val search = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val data = search.flatMapLatest {
        searchRepo.search(it)
    }

    val adapter = RVSearchQueries()

    fun searchAllData() {
        query.value = ""
    }

}

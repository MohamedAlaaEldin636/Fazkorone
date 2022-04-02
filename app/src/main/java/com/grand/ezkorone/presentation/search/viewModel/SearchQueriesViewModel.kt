package com.grand.ezkorone.presentation.search.viewModel

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.*
import com.google.android.material.textfield.TextInputEditText
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.data.home.repository.RepositoryHome
import com.grand.ezkorone.data.search.repository.RepositorySearch
import com.grand.ezkorone.presentation.search.adapter.RVSearchQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class SearchQueriesViewModel @Inject constructor(
    searchRepo: RepositorySearch
) : ViewModel() {

    private val search = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val data = search.flatMapLatest {
        searchRepo.search(it)
    }

    val adapter = RVSearchQueries()

    fun onSearchTextSubmit() = TextView.OnEditorActionListener { textView, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search.value = textView.text?.toString().orEmpty()

            return@OnEditorActionListener true
        }

        false
    }

    fun searchAllData(editText: TextInputEditText) {
        editText.setText("")

        search.value = ""
    }

}

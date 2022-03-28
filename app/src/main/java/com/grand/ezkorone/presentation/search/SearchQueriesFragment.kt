package com.grand.ezkorone.presentation.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentSearchQueriesBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.search.viewModel.SearchQueriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchQueriesFragment : MABaseFragment<FragmentSearchQueriesBinding>() {

    private val viewModel by viewModels<SearchQueriesViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_search_queries

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //handleRetryAbleFlowWithMustHaveResultWithNullability() todo see on page change, acc. to page index and list size as well isa.
        /*viewModel.skipOrStartButtonText.value = if (viewModel.list.isEmpty()) {
            getString(R.string.skip)
        }else {
            getString(R.string.start_application)
        }

        binding?.sliderView?.setCurrentPageListener {
            // todo change text accordingly isa.
        }*/
    }

}

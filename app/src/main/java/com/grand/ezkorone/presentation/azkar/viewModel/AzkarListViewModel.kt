package com.grand.ezkorone.presentation.azkar.viewModel

import androidx.lifecycle.ViewModel
import com.grand.ezkorone.data.home.repository.RepositoryHome
import com.grand.ezkorone.presentation.azkar.AzkarListFragmentArgs
import com.grand.ezkorone.presentation.azkar.adapter.RVZekrInList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AzkarListViewModel @Inject constructor(
    repoHome: RepositoryHome,
    args: AzkarListFragmentArgs,
) : ViewModel() {

    val azkar = repoHome.getAzkarList(args.id)

    val adapter = RVZekrInList(args.id)

}

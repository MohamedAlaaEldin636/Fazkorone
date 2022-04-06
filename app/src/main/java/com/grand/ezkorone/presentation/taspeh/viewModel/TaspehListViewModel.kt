package com.grand.ezkorone.presentation.taspeh.viewModel

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.grand.ezkorone.data.sheikh.repository.RepositorySheikh
import com.grand.ezkorone.data.taspeh.repository.RepositoryTaspeh
import com.grand.ezkorone.presentation.sheikh.SheikhListFragmentArgs
import com.grand.ezkorone.presentation.sheikh.adapter.RVItemSheikh
import com.grand.ezkorone.presentation.taspeh.TaspehListFragmentArgs
import com.grand.ezkorone.presentation.taspeh.adapter.RVItemTaspeh
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaspehListViewModel @Inject constructor(
    repoTaspeh: RepositoryTaspeh,
    args: TaspehListFragmentArgs,
    gson: Gson,
) : ViewModel() {

    val taspehList = repoTaspeh.getTaspehListPaginated()

    val adapter = RVItemTaspeh(gson)

}

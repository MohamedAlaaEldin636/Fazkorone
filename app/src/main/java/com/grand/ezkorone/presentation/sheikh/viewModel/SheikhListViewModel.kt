package com.grand.ezkorone.presentation.sheikh.viewModel

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.grand.ezkorone.data.sheikh.repository.RepositorySheikh
import com.grand.ezkorone.presentation.sheikh.SheikhListFragmentArgs
import com.grand.ezkorone.presentation.sheikh.adapter.RVItemSheikh
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SheikhListViewModel @Inject constructor(
    repoSheikh: RepositorySheikh,
    args: SheikhListFragmentArgs,
    gson: Gson,
) : ViewModel() {

    val sheikhList = if (args.taspehId >= 0) {
        repoSheikh.getSheikhListForTaspeh(args.taspehId)
    }else {
        repoSheikh.getSheikhListForSalahFardType(args.salahFardType)
    }

    val adapter = RVItemSheikh(repoSheikh, gson, args.taspehId < 0)

}

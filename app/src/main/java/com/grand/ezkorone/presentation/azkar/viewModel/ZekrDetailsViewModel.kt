package com.grand.ezkorone.presentation.azkar.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grand.ezkorone.core.customTypes.RetryAbleFlow
import com.grand.ezkorone.core.customTypes.map
import com.grand.ezkorone.core.customTypes.switchMapMultiple
import com.grand.ezkorone.core.customTypes.switchMapMultiple2
import com.grand.ezkorone.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.grand.ezkorone.data.favorite.repository.RepositoryFavorite
import com.grand.ezkorone.data.home.repository.RepositoryHome
import com.grand.ezkorone.domain.azkar.ResponseZekrDetail
import com.grand.ezkorone.presentation.azkar.ZekrDetailsFragment
import com.grand.ezkorone.presentation.azkar.ZekrDetailsFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZekrDetailsViewModel @Inject constructor(
    repoHome: RepositoryHome,
    private val repoFavorite: RepositoryFavorite,
    private val args: ZekrDetailsFragmentArgs,
) : ViewModel() {

    val retryAblFlowResponseZekrDetails = RetryAbleFlow {
        repoHome.getZekrDetails(args.id)
    }

    val responseZekrDetail = MutableLiveData<ResponseZekrDetail>()

    val showLoading = MutableLiveData(false)

    val currentIndex = MutableLiveData(1)

    val count = MutableLiveData(0)

    val progressText = switchMapMultiple2(responseZekrDetail, currentIndex, count) {
        responseZekrDetail.value?.let {
            val maxCount = if (it.data.size < 2) {
                it.data[0].maxCount
            }else {
                it.data[currentIndex.value!!].maxCount
            }

            "${count.value!!}/$maxCount"
        }.orEmpty()
    }

    val progressPercentage = switchMapMultiple2(responseZekrDetail, currentIndex, count) {
        responseZekrDetail.value?.let {
            val maxCount = if (it.data.size < 2) {
                it.data[0].maxCount
            }else {
                it.data[currentIndex.value!!].maxCount
            }

            when {
                maxCount <= 0 || count.value!! > maxCount -> 100
                else -> count.value!! * 100 / maxCount
            }
        }
    }

    /** true means fav, false not, null means check in the response this is used instead of reloading again isa. */
    private val forceIsFavorite = MutableLiveData<Boolean>()

    val showIsFavorite = switchMapMultiple2(responseZekrDetail, currentIndex) {
        forceIsFavorite.value ?: responseZekrDetail.value?.let {
            it.data[0].isFavorite
        }
    }

    fun toggleFavorite(view: View) {
        view.findFragment<ZekrDetailsFragment>().executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                if (args.isFromHorzNotVertHomeScreenList) {
                    repoFavorite.toggleFavoriteForHorizontalList(args.id)
                }else {
                    repoFavorite.toggleFavoriteForVerticalList(args.id)
                }
            },
            afterHidingLoading = {
                forceIsFavorite.value = forceIsFavorite.value?.not()
                    ?: responseZekrDetail.value!!.data[0].isFavorite.not()
            },
            canCancelDialog = true,
        )
    }

    fun download(view: View) {
        // todo
    }

    fun play(view: View) {
        // todo
    }

    fun share(view: View) {
        // todo
    }

    fun incrementProgress() {
        count.value = count.value!!.inc()
    }

}

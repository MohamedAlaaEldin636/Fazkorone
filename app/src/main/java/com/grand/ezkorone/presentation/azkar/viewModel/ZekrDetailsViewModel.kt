package com.grand.ezkorone.presentation.azkar.viewModel

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.getSystemService
import androidx.fragment.app.findFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.RetryAbleFlow
import com.grand.ezkorone.core.customTypes.map
import com.grand.ezkorone.core.customTypes.switchMapMultiple
import com.grand.ezkorone.core.customTypes.switchMapMultiple2
import com.grand.ezkorone.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.grand.ezkorone.core.extensions.launchShareText
import com.grand.ezkorone.core.extensions.showSuccessToast
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

    val showAudioLoading = MutableLiveData(false)
    val showAudioPlayNotPause = MutableLiveData(true)

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
        // Download el PDF
        val response = responseZekrDetail.value ?: return

        val item = if (response.data.size < 2) {
            response.data[0]
        }else {
            response.data[currentIndex.value!!]
        }

        val name = "${args.toolbarTitle}_${item.id}"
        val request = DownloadManager.Request(Uri.parse(item.pdfUrl))
            .setTitle(view.context.getString(R.string.app_name))
            .setDescription(name)
            //.allowScanningByMediaScanner()
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name)

        val downloadManager = view.context.getSystemService<DownloadManager>()
            ?: return

        downloadManager.enqueue(request)

        view.context.showSuccessToast(view.context.getString(R.string.loading))
    }

    fun play(view: View) {
        val response = responseZekrDetail.value ?: return

        val item = if (response.data.size < 2) {
            response.data[0]
        }else {
            response.data[currentIndex.value!!]
        }

        view.findFragment<ZekrDetailsFragment>().toggleSinglePlayerPlayAndPauseStatue(item.audioUrl)
    }

    fun share(view: View) {
        val response = responseZekrDetail.value ?: return

        val item = if (response.data.size < 2) {
            response.data[0]
        }else {
            response.data[currentIndex.value!!]
        }

        view.context.launchShareText(item.pdfUrl)
    }

    fun incrementProgress() {
        count.value = count.value!!.inc()
    }

}

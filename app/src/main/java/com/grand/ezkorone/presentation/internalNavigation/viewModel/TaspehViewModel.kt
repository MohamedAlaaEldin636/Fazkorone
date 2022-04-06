package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.switchMapMultiple
import com.grand.ezkorone.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.openDrawerLayout
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.data.sheikh.repository.RepositorySheikh
import com.grand.ezkorone.data.taspeh.repository.RepositoryTaspeh
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.domain.taspeh.ItemTaspeh
import com.grand.ezkorone.domain.taspeh.ResponseTaspeh
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.domain.utils.mapImmediate
import com.grand.ezkorone.domain.utils.toSuccessOrNull
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import com.grand.ezkorone.presentation.internalNavigation.TaspehFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaspehViewModel @Inject constructor(
    val repoTaspeh: RepositoryTaspeh,
) : ViewModel() {

    private val _count = MutableLiveData(0)
    val count = _count.map { it.toString() }

    private val currentIndex = MutableLiveData(0)

    val responseTaspeh = MutableLiveData<ResponseTaspeh>()

    private val currentItem = switchMapMultiple(responseTaspeh, currentIndex) {
        MutableLiveData(responseTaspeh.value?.list.orEmpty().getOrNull(currentIndex.value!!))
    }

    val canShowPreviousTaspeh = currentIndex.map {
        it > 0
    }

    val canShowNextTaspeh = switchMapMultiple(responseTaspeh, currentIndex) {
        MutableLiveData(
            currentIndex.value!! < responseTaspeh.value?.list.orEmpty().lastIndex
                || responseTaspeh.value?.hasNextPage == true
        )
    }

    val maxCount = currentItem.map {
        it?.count?.toString().orEmpty()
    }

    val taspehText = currentItem.map {
        it?.name.orEmpty()
    }

    fun reset() {
        _count.value = 0
    }
    fun decrement() {
        _count.value = _count.value!!.dec().coerceAtLeast(0)
    }
    fun increment() {
        _count.value = _count.value!!.inc()
    }

    fun showNavDrawer(view: View) {
        view.openDrawerLayout()
    }

    fun prevZekr() {
        reset()

        currentIndex.value = currentIndex.value!!.dec()
    }

    fun nextZekr(view: View) {
        if (currentIndex.value!! < responseTaspeh.value?.list.orEmpty().lastIndex) {
            reset()

            currentIndex.value = currentIndex.value!!.inc()
        }else {
            view.findFragment<TaspehFragment>().executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                afterShowingLoading = {
                    repoTaspeh.getAzkarList(responseTaspeh.value!!.currentPage.inc())
                },
                afterHidingLoading = {
                    if (it != null) {
                        val currentResponseTaspeh = responseTaspeh.value!!

                        responseTaspeh.value = it.copy(
                            list = currentResponseTaspeh.list + it.list
                        )

                        nextZekr(view)
                    }else {
                        view.context.showErrorToast(view.context.getString(R.string.something_went_wrong))
                    }
                },
                canCancelDialog = true
            )
        }
    }

    fun pickAnotherZekr(view: View) {
        view.findNavControllerOfProject().navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestTaspehList(currentItem.value?.id ?: return)
        )
    }

    fun sheikh(view: View) {
        view.findNavControllerOfProject().navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestSheikhList(
                SalahFardType.FAGR, // has no effect as long as below id >= 0 isa.
                currentItem.value?.id ?: return
            )
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

    fun changeCurrentItem(fragment: TaspehFragment, itemTaspeh: ItemTaspeh) {
        val index = responseTaspeh.value?.list?.indexOfFirst { it.id == itemTaspeh.id }
        if (index != null && index >= 0) {
            currentIndex.value = index
        }else {
            val startPage = responseTaspeh.value!!.currentPage.inc()

            fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                afterShowingLoading = {
                    val list = mutableListOf<ItemTaspeh>()
                    list += responseTaspeh.value!!.list
                    while (true) {
                        val result = repoTaspeh.getAzkarList(startPage)

                        if (result is MAResult.Success) {
                            list += result.value.data?.list.orEmpty()

                            val indexOfItem = list.indexOfFirst { it.id == itemTaspeh.id }

                            if (indexOfItem != -1) {
                                this.currentIndex.value

                                return@executeOnGlobalLoadingAndAutoHandleRetryCancellable result.mapImmediate { maBaseResponse ->
                                    maBaseResponse.mapData {
                                        it?.copy(
                                            list = list
                                        ) to indexOfItem
                                    }
                                }
                            }
                        }else {
                            // Will auto retry if user want to do so isa.
                            return@executeOnGlobalLoadingAndAutoHandleRetryCancellable result.mapImmediate { maBaseResponse ->
                                maBaseResponse.mapData { it to 0 }
                            }
                        }

                    }

                    @Suppress("UNREACHABLE_CODE")
                    repoTaspeh.getAzkarList(startPage).mapImmediate { maBaseResponse ->
                        maBaseResponse.mapData { it to 0 }
                    }
                },
                afterHidingLoading = { pair ->
                    if (pair?.first != null) {
                        responseTaspeh.value = pair.first!!

                        currentIndex.value = pair.second
                    }else {
                        fragment.requireContext().showErrorToast(fragment.getString(R.string.something_went_wrong))
                    }
                },
                canCancelDialog = true
            )
        }
    }

}

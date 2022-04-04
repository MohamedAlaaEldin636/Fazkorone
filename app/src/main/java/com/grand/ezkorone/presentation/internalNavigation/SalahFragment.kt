package com.grand.ezkorone.presentation.internalNavigation

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.DownloadingUtils
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.databinding.FragmentHomeBinding
import com.grand.ezkorone.databinding.FragmentSalahBinding
import com.grand.ezkorone.domain.salah.toSimpleDate
import com.grand.ezkorone.domain.sheikh.ItemSheikh
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.HomeViewModel
import com.grand.ezkorone.presentation.internalNavigation.viewModel.SalahViewModel
import com.grand.ezkorone.presentation.sheikh.SheikhListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SalahFragment : MABaseFragment<FragmentSalahBinding>() {

    private val viewModel by viewModels<SalahViewModel>()

    @Inject
    protected lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_salah

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.currentDateTime.distinctUntilChanged().observe(viewLifecycleOwner) {
            val localDate = LocalDate.from(it)

            val salawatTimes = viewModel.cacheOfDateAndSalawatTimes[localDate.toSimpleDate()]

            if (salawatTimes != null) {
                viewModel.updateSalawatTimes(salawatTimes)
            }else {
                executeOnGlobalLoadingAndAutoHandleRetry(
                    afterShowingLoading = {
                        viewModel.repoAzan.getAzanTimesSuspend(localDate)
                    },
                    afterHidingLoading = { response ->
                        viewModel.cacheOfDateAndSalawatTimes[localDate.toSimpleDate()] = response.timings

                        viewModel.updateSalawatTimes(response.timings)
                    },
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                Timber.d("SPTAG1 Entered here ch 1")

                findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.getLiveData(
                    SheikhListFragment.SAVED_STATE_SELECTED_JSON_ITEM_SHEIKH,
                    ""
                )?.observe(viewLifecycleOwner) {
                    if (!it.isNullOrEmpty()) {
                        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.set(
                            SheikhListFragment.SAVED_STATE_SELECTED_JSON_ITEM_SHEIKH,
                            ""
                        )

                        Timber.d("SPTAG1 ${findNavControllerOfProject().currentBackStackEntry?.destination?.label}")

                        val itemSheikh = it.fromJson<ItemSheikh>(gson)

                        val file = File(requireContext().filesDir, "${viewModel.salahFardType}_${itemSheikh.id}")

                        Timber.d("file.exists() ${file.exists()}")
                        if (!file.exists()) {
                            downloadAudio(itemSheikh.audioUrl, file)
                        }else {
                            executeOnGlobalLoadingAfterShowingLoading {
                                viewModel.prefsSalah.setSalahFardTypeNotificationSoundUri(
                                    viewModel.salahFardType, Uri.fromFile(file).toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadAudio(link: String, destinationFile: File) {
        executeOnGlobalLoadingAfterShowingLoading {
            if (DownloadingUtils.downloadFile(link, destinationFile)) {
                val uri = Uri.fromFile(destinationFile)

                Timber.d("Download done successfully isa. with uri $uri")

                viewModel.prefsSalah.setSalahFardTypeNotificationSoundUri(
                    viewModel.salahFardType, uri.toString()
                )
            }else {
                requireContext().showErrorToast(getString(R.string.something_went_wrong))
            }
        }
    }

}

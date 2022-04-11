package com.grand.ezkorone.presentation.internalNavigation

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    private var tempItemSheikh: ItemSheikh? = null

    private val permissionsStorageRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
                performDownload(tempItemSheikh ?: return@registerForActivityResult)
            }
            else -> {
                requireContext().showNormalToast(getString(R.string.you_didn_t_accept_permission))
            }
        }
    }

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

                        val itemSheikh = it.fromJson<ItemSheikh>(gson)

                        val downloadManager = view.context.getSystemService<DownloadManager>()

                        val idOfDownloadManager = runBlocking {
                            viewModel.prefsSalah.getSalahFardTypeNotificationDownloadManagerId(viewModel.salahFardType, itemSheikh.id).first()
                        }
                        val uri = idOfDownloadManager?.let {
                            kotlin.runCatching {
                                downloadManager?.getUriForDownloadedFile(idOfDownloadManager)
                            }.getOrNull()
                        }

                        if (uri == null) {
                            if (requireContext().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                                && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                performDownload(itemSheikh)
                            }else {
                                tempItemSheikh = itemSheikh

                                permissionsStorageRequest.launch(arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ))
                            }
                        }else {
                            viewModel.viewModelScope.launch {
                                viewModel.prefsSalah.setSalawatNotificationDownloadManagerId(
                                    viewModel.salahFardType, idOfDownloadManager
                                )
                            }
                            /*executeOnGlobalLoadingAfterShowingLoading {
                                viewModel.prefsSalah.setSalahFardTypeNotificationSoundUri(
                                    viewModel.salahFardType, Uri.fromFile(file).toString()
                                )
                            }*/
                        }
                    }
                }
            }
        }
    }

    private fun performDownload(itemSheikh: ItemSheikh) {
        val context = requireContext()
        val downloadManager = context.getSystemService<DownloadManager>()

        val request = DownloadManager.Request(Uri.parse(itemSheikh.audioUrl))
            .setTitle(context.getString(R.string.app_name))
            .setDescription(itemSheikh.name)
            //.allowScanningByMediaScanner()
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, itemSheikh.name)

        val id = downloadManager?.enqueue(request)

        requireContext().showSuccessToast(getString(R.string.loading))

        viewModel.viewModelScope.launch {
            viewModel.prefsSalah.setSalawatNotificationDownloadManagerId(
                viewModel.salahFardType, id
            )
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

package com.grand.ezkorone.presentation.azkar

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.RTLRemotePDFViewPager
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.databinding.FragmentZekrDetailsBinding
import com.grand.ezkorone.presentation.azkar.viewModel.ZekrDetailsViewModel
import com.grand.ezkorone.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class ZekrDetailsFragment : MABaseFragment<FragmentZekrDetailsBinding>(), DownloadFile.Listener {

    private val viewModel by viewModels<ZekrDetailsViewModel>()

    private val args by navArgs<ZekrDetailsFragmentArgs>()

    private var remotePDFViewPager: RemotePDFViewPager? = null

    private var adapter: PDFPagerAdapter? = null

    private var playerAll: ExoPlayer? = null
    private var playerSingle: ExoPlayer? = null

    /** true show play, false pause, else loading */
    private var allPlayerShowPlayNotPause = true

    private var currentAudioUrl: String? = null

    private val singlePlayerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == ExoPlayer.STATE_READY) {
                viewModel.showAudioLoading.value = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_zekr_details

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel.titleToolbar.postValue(args.toolbarTitle)

        viewModel.currentIndex.distinctUntilChanged().observe(viewLifecycleOwner) {
            playerSingle?.removeListener(singlePlayerListener)
            playerSingle?.pause()
            viewModel.showAudioLoading.value = false
            viewModel.showAudioPlayNotPause.value = true
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAblFlowResponseZekrDetails) {
            viewModel.showLoading.value = true

            viewModel.responseZekrDetail.value = it.data

            initializePlayer()

            if (it.data?.data.orEmpty().isEmpty()) {
                context?.showErrorToast(getString(R.string.empty_data))

                findNavController().navigateUp()
            }else {
                remotePDFViewPager = RTLRemotePDFViewPager(requireContext(), it.data!!.data[0].pdfUrl, this)
            }
        }
    }

    override fun onSuccess(url: String?, destinationPath: String?) {
        viewModel.showLoading.value = false

        adapter = PDFPagerAdapter(requireContext(), destinationPath)

        remotePDFViewPager?.adapter = adapter

        binding?.containerFrameLayout?.removeAllViews()
        binding?.containerFrameLayout?.addView(remotePDFViewPager)

        if (viewModel.responseZekrDetail.value?.data.orEmpty().size > 1) {
            remotePDFViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageSelected(position: Int) {
                    viewModel.currentIndex.value = position + 1

                    viewModel.count.value = 0
                }
            })
        }
    }

    override fun onFailure(e: Exception?) {
        viewModel.showLoading.value = false

        // This will be called if download fails
        Timber.e("Unexpected error occurred while downloading PDF -> $e")

        val errorMsg = e?.message.let {
            if (it.isNullOrEmpty()) getString(R.string.something_went_wrong) else it
        }
        requireContext().showErrorToast(errorMsg)

        // Retry download isa.
        remotePDFViewPager = RemotePDFViewPager(
            requireContext(), viewModel.responseZekrDetail.value!!.data[0].pdfUrl, this
        )
    }

    override fun onProgressUpdate(progress: Int, total: Int) {
        // You will get download progress here
        // Always on UI Thread so feel free to update your views here
        viewModel.showLoading.value = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()

        val menuRes = if (allPlayerShowPlayNotPause) {
            R.menu.menu_zekr_details
        }else {
            R.menu.menu_zekr_details_pause
        }
        inflater.inflate(menuRes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> {
                playerAll?.play()

                allPlayerShowPlayNotPause = false

                requireActivity().invalidateOptionsMenu()
            }
            R.id.action_pause -> {
                playerAll?.pause()

                allPlayerShowPlayNotPause = true

                requireActivity().invalidateOptionsMenu()
            }
            R.id.action_share -> viewModel.responseZekrDetail.value?.also { response ->
                context?.launchShareText(response.data[0].pdfUrl)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        adapter?.close()
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || playerAll == null || playerSingle == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        playerAll = /*Simple*/ExoPlayer.Builder(requireContext())
            .build()
        playerSingle = /*Simple*/ExoPlayer.Builder(requireContext())
            .build()

        viewModel.responseZekrDetail.value?.also { response ->
            playerAll?.also { exoPlayer ->
                val mediaItem = MediaItem.fromUri(response.data[0].audioUrl)
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = false
                exoPlayer.prepare()
            }
        }
    }

    private fun releasePlayer() {
        playerAll?.release()
        playerAll = null

        playerSingle?.release()
        playerSingle = null
    }

    fun toggleSinglePlayerPlayAndPauseStatue(audioUrl: String) {
        playerSingle?.also { exoPlayer ->
            when {
                currentAudioUrl == null || currentAudioUrl != audioUrl -> {
                    exoPlayer.removeListener(singlePlayerListener)

                    val mediaItem = MediaItem.fromUri(audioUrl)
                    exoPlayer.setMediaItem(mediaItem)

                    viewModel.showAudioLoading.value = true
                    viewModel.showAudioPlayNotPause.value = false
                    exoPlayer.addListener(singlePlayerListener)

                    exoPlayer.playWhenReady = true
                    exoPlayer.prepare()
                }
                else -> /* currentAudioUrl == audioUrl */ {
                    val playIconIsShown = viewModel.showAudioPlayNotPause.value!!

                    if (playIconIsShown) {
                        exoPlayer.play()
                    }else {
                        exoPlayer.pause()
                    }

                    viewModel.showAudioPlayNotPause.value = !playIconIsShown
                }
            }

            currentAudioUrl = audioUrl
        }
    }

}

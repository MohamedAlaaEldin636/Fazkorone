package com.grand.ezkorone.presentation.internalNavigation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.databinding.FragmentQiblaBinding
import com.grand.ezkorone.databinding.FragmentTaspehBinding
import com.grand.ezkorone.domain.taspeh.ItemTaspeh
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.QiblaViewModel
import com.grand.ezkorone.presentation.internalNavigation.viewModel.TaspehViewModel
import com.grand.ezkorone.presentation.taspeh.TaspehListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaspehFragment : MABaseFragment<FragmentTaspehBinding>() {

    private val viewModel by viewModels<TaspehViewModel>()

    private var player: ExoPlayer? = null

    private val listenerOfPlayer = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == ExoPlayer.STATE_ENDED) {
                player?.pause()
                player?.seekTo(0L)
                viewModel.showPlayNotPause.value = true
                isPlaying = false
            }
        }
    }

    val permissionsStorageRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
                viewModel.download(binding?.root ?: return@registerForActivityResult)
            }
            else -> {
                requireContext().showNormalToast(getString(R.string.you_didn_t_accept_permission))
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_taspeh

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        executeOnGlobalLoadingAndAutoHandleRetry(
            afterShowingLoading = {
                viewModel.repoTaspeh.getAzkarList(1)
            },
            afterHidingLoading = {
                viewModel.responseTaspeh.value = it

                observeSelectedTaspehIfUsed()
            }
        )
    }

    private fun observeSelectedTaspehIfUsed() {
        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.getLiveData(
            TaspehListFragment.SAVED_STATE_SELECTED_JSON_ITEM_TASPEH,
            ""
        )?.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.set(
                    TaspehListFragment.SAVED_STATE_SELECTED_JSON_ITEM_TASPEH,
                    ""
                )

                val itemTaspeh = it.fromJson<ItemTaspeh>()

                viewModel.changeCurrentItem(this, itemTaspeh)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || player == null) {
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
        player = /*Simple*/ExoPlayer.Builder(requireContext())
            .build()
        player?.addListener(listenerOfPlayer)
    }

    private var currentAudioUrl: String? = null
    private var isPlaying = false

    fun pauseAudio() {
        player?.pause()

        viewModel.showPlayNotPause.value = true

        isPlaying = false
    }

    fun playAudio(audioUrl: String) {
        if (currentAudioUrl == audioUrl) {
            if (isPlaying) {
                player?.pause()
            }else {
                player?.play()
            }

            isPlaying = !isPlaying

            viewModel.showPlayNotPause.value = !isPlaying
        }else {
            player?.also { exoPlayer ->
                isPlaying = true

                viewModel.showPlayNotPause.value = false

                exoPlayer.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == ExoPlayer.STATE_READY) {
                            viewModel.loadingAudio.value = false
                        }
                    }
                })

                val mediaItem = MediaItem.fromUri(audioUrl)
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = true
                exoPlayer.prepare()

                viewModel.loadingAudio.value = true
            }
        }

        currentAudioUrl = audioUrl
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

}

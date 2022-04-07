package com.grand.ezkorone.presentation.azkar

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.RTLRemotePDFViewPager
import com.grand.ezkorone.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.grand.ezkorone.core.extensions.launchShareText
import com.grand.ezkorone.core.extensions.showErrorToast
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

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAblFlowResponseZekrDetails) {
            viewModel.showLoading.value = true

            viewModel.responseZekrDetail.value = it.data

            remotePDFViewPager = RTLRemotePDFViewPager(requireContext(), it.data!!.data[0].pdfUrl, this)
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

        inflater.inflate(R.menu.menu_zekr_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // todo prepare once loaded asln isa. for update icon ba2a 3eee4 nta isa.
            //  khalena f elle ta7t tb el awal isa.
            R.id.action_play -> TODO("see other play isa.")
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

}

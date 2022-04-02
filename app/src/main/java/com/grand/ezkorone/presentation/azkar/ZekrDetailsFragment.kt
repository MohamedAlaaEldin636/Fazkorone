package com.grand.ezkorone.presentation.azkar

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.PagerAdapter
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentSearchQueriesBinding
import com.grand.ezkorone.databinding.FragmentZekrDetailsBinding
import com.grand.ezkorone.presentation.azkar.viewModel.ZekrDetailsViewModel
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.search.viewModel.SearchQueriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.voghdev.pdfviewpager.library.PDFViewPagerZoom
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter

// todo USE -> fragment_page_view_zekr_details as the page in adapter isa.
@AndroidEntryPoint
class ZekrDetailsFragment : MABaseFragment<FragmentZekrDetailsBinding>() {

    private val viewModel by viewModels<ZekrDetailsViewModel>()

    private val args by navArgs<ZekrDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_zekr_details

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    // todo VIEW PAGER -> https://developer.android.com/training/animation/screen-slide-2#depth-page
    // TODO
    // TODO
    // TODO
    // TODO FOR INTERNAL FRAGMENT -> https://github.com/voghDev/PdfViewPager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel.titleToolbar.postValue(args.toolbarTitle)

        // todo view pager + actual page page transformation + inside fragment has a zoom effect so zoomable panable etc... isa.
        /*val k: PDFViewPagerZoom
        val a = PDFViewPagerZoom(requireActivity(), "")
        a.adapter = PDFPagerAdapter(requireContext())
        val mm = RemotePDFViewPager(requireContext(), "url", this)
        mm.adapter*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()

        inflater.inflate(R.menu.menu_zekr_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> TODO()
            R.id.action_share -> TODO()
        }

        return super.onOptionsItemSelected(item)
    }

}

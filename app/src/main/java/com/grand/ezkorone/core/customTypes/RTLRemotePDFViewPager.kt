package com.grand.ezkorone.core.customTypes

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile

@SuppressLint("ViewConstructor")
class RTLRemotePDFViewPager(
    context: Context,
    pdfUrl: String,
    listener: DownloadFile.Listener
) : RemotePDFViewPager(context, pdfUrl, listener) {

    override fun onRtlPropertiesChanged(layoutDirection: Int) {
        super.onRtlPropertiesChanged(layoutDirection)
        rotationY = 180f
    }

    override fun onViewAdded(child: View?) {
        child?.rotationY = 180f
        super.onViewAdded(child)
    }

    override fun getChildCount(): Int {
        return super.getChildCount()
    }

}

class AB(context: Context, path: String) : PDFPagerAdapter(context, path) {
    override fun getCount(): Int {
        return super.getCount()
    }
}

private fun k(ab: AB) {
    ab.count
}
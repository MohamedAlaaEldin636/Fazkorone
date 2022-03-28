package com.grand.ezkorone.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.grand.ezkorone.data.local.preferences.PrefsSplash
import com.grand.ezkorone.presentation.main.viewModel.MainViewModel
import javax.inject.Inject

abstract class MABaseFragment<VDB : ViewDataBinding> : Fragment() {

    var binding: VDB? = null

    @Inject
    lateinit var prefsSplash: PrefsSplash

    val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        initializeBindingVariables()
        binding?.lifecycleOwner = viewLifecycleOwner

        return binding?.root
    }

    protected open fun initializeBindingVariables() {}

    override fun onDestroyView() {
        binding = null

        super.onDestroyView()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

}

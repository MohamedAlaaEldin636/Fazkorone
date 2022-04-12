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
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grand.ezkorone.data.local.preferences.PrefsSplash
import com.grand.ezkorone.presentation.main.viewModel.MainViewModel
import javax.inject.Inject

abstract class MABaseFragment<VDB : ViewDataBinding> : Fragment() {

    var binding: VDB? = null

    @Inject
    lateinit var prefsSplash: PrefsSplash

    private val lazyActivityViewModel by lazy {
        activityViewModels<MainViewModel>()
    }

    /*todo el 7al en di tb2a nullable w ba3den n4of ba2a eh elle haye7sal isa.
    *  or on activity creation create new graph ba2a isa.
    */
    val activityViewModel by activityViewModels<MainViewModel>()

    private inline fun <reified VM : ViewModel> Fragment.safeActivityViewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM?> = if (isAdded) createViewModelLazy(
        VM::class, { requireActivity().viewModelStore },
        factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
    ) else lazy<VM?> { null }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        initializeBindingVariables()
        binding?.lifecycleOwner = viewLifecycleOwner

        // todo maybe check if isAdded and if not restart activity recreate
        //requireActivity().recreate()
        //if (isAdded)

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

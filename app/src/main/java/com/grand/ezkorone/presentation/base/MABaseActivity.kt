package com.grand.ezkorone.presentation.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.grand.ezkorone.core.MyApplication
import com.grand.ezkorone.core.customTypes.LanguagesHelper
import com.grand.ezkorone.core.customTypes.MyContextWrapper
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import java.util.*

abstract class MABaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

    companion object {
        const val APPLICATION_DEFAULT_LANGUAGE = "ar"
    }

    //private val localeDelegate: LocaleHelperActivityDelegate = LocaleHelperActivityDelegateImpl()

    var binding: VDB? = null

    override fun attachBaseContext(newBase: Context?) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && newBase != null) {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, LanguagesHelper.getCurrentLanguage()));
        }else {
            super.attachBaseContext(newBase)
        }
    }

    /*
    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, LanguagesHelper.getCurrentLanguage()));
        } else {
            super.attachBaseContext(newBase);
        }
    }
     */

    /*@CallSuper
    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        val context = super.createConfigurationContext(overrideConfiguration)
        //localeDelegate.setLocale(this, Locale(getProjectCurrentLocale()))
        return LocaleHelper.onAttach(context)
    }

    @CallSuper
    override fun getApplicationContext(): Context =
        localeDelegate.getApplicationContext(super.getApplicationContext())*/

    @CallSuper
    final override fun onCreate(savedInstanceState: Bundle?) {
        //Locale.setDefault(Locale(APPLICATION_DEFAULT_LANGUAGE))
        //LocaleHelper.setLocale(this, Locale(APPLICATION_DEFAULT_LANGUAGE))

        super.onCreate(savedInstanceState)

        LanguagesHelper.changeLanguage(this, LanguagesHelper.getCurrentLanguage());
        LanguagesHelper.changeLanguage(applicationContext as MyApplication, LanguagesHelper.getCurrentLanguage());

        binding = DataBindingUtil.setContentView(this, getLayoutId())
        initializeBindingVariables()
        binding?.lifecycleOwner = this

        setupsInOnCreate()
    }

    protected open fun initializeBindingVariables() {}

    /** Called inside [onCreate] after initializing [binding] isa. */
    protected open fun setupsInOnCreate() {}

    @CallSuper
    override fun onResume() {
        super.onResume()

        //localeDelegate.onResumed(this)
    }

    @CallSuper
    override fun onPause() {
        //localeDelegate.onPaused()

        super.onPause()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    /*open fun updateLocale(language: String) {
        localeDelegate.setLocale(this, Locale(language))
    }

    @CallSuper
    override fun getDelegate() = localeDelegate.getAppCompatDelegate(super.getDelegate())*/

}

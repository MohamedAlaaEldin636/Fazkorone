package com.grand.ezkorone.presentation.main

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.appbar.MaterialToolbar
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.GlobalError
import com.grand.ezkorone.core.extensions.inflateGraph
import com.grand.ezkorone.core.extensions.navigateDeepLinkWithoutOptions
import com.grand.ezkorone.core.extensions.setupWithNavControllerMA
import com.grand.ezkorone.databinding.ActivityMainBinding
import com.grand.ezkorone.databinding.DrawerHeaderMainBinding
import com.grand.ezkorone.presentation.base.MABaseActivity
import com.grand.ezkorone.presentation.main.viewModel.DrawerHeaderMainViewModel
import com.grand.ezkorone.presentation.main.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : MABaseActivity<ActivityMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val navViewViewModel by viewModels<DrawerHeaderMainViewModel>()

    private val destinationsHideToolbar = setOf(
        R.id.dest_splash,
        R.id.dest_location_selection,
        R.id.dest_who_are_we,
    )

    private val destinationsIgnoreToolbarVisibility = setOf(
        R.id.dest_global_error_dialog,
        R.id.dest_lottie_loader_dialog,
        R.id.dest_bottom_nav,
        R.id.dest_alarm_time_picker_dialog,
    )

    private val topLevelDestinations = setOf(
        R.id.dest_bottom_nav,
    )

    private val darkToolbarDestinations = setOf(
        -1,
        /*R.id.dest_search_query,
        R.id.dest_search_results_providers,
        R.id.dest_filter_picker_dialog,*/
    )

    private val centeredTitleToolbarDestinations = setOf(
        R.id.dest_on_board,
        R.id.dest_search_queries,
        R.id.dest_pick_zekr,
        R.id.dest_contact_us,
        R.id.dest_favorite,
        R.id.dest_alarms,
        R.id.dest_notifications,
    )

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun setupsInOnCreate() {
        // Support action bar so that fragments have easier access for toolbar menu items.
        setSupportActionBar(binding?.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setups
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.inflateGraph(R.navigation.nav_main)
        binding?.materialToolbar?.setupWithNavControllerMA(
            navController,
            AppBarConfiguration(topLevelDestinations, binding?.drawerLayout)
        )

        val navViewBinding = DataBindingUtil.inflate<DrawerHeaderMainBinding>(layoutInflater, R.layout.drawer_header_main, binding?.navigationView, false)
        navViewBinding.viewModel = navViewViewModel
        navViewBinding.lifecycleOwner = this
        binding?.navigationView?.addHeaderView(navViewBinding.root)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            run {
                // They should be changed after calling api from other fragments isa.
                //viewModel.menuItemNotificationsVisibility.value = MenuItemVisibility.HIDE

                if (destination.id !in destinationsIgnoreToolbarVisibility) {
                    viewModel.showToolbar.value = destination.id !in destinationsHideToolbar
                }
                destination.label?.toString().orEmpty().also {
                    if (it.isNotEmpty()) {
                        viewModel.titleToolbar.postValue(it)
                    }
                }

                if (destination.id !in topLevelDestinations) {
                    /*val forceNormalToolbar = destination.id == R.id.dest_search_results_providers
                        && SearchResultsProvidersFragmentArgs.fromBundle(arguments.orEmpty()).categoryName != null*/
                    val (color, drawable) = if (destination.id in darkToolbarDestinations/* && !forceNormalToolbar*/) {
                        getColor(R.color.black) to ColorDrawable(getColor(R.color.white))
                    }else {
                        getColor(R.color.white) to ColorDrawable(getColor(R.color.colorPrimaryDark))
                    }
                    binding?.materialToolbar?.post {
                        (binding?.materialToolbar?.navigationIcon as? DrawerArrowDrawable)?.color = color
                        binding?.materialToolbar?.setTitleTextColor(color)
                        binding?.materialToolbar?.background = drawable
                    }
                }

                binding?.materialToolbar?.post {
                    binding?.materialToolbar?.isTitleCentered = destination.id in centeredTitleToolbarDestinations
                }
            }
        }

        viewModel.globalLoading.distinctUntilChanged().observe(this) {
            if (it == true) {
                navController.navigateDeepLinkWithoutOptions(
                    "dialog-dest",
                    "com.grand.ezkorone.lottie.loader.dialog"
                )
            }
        }

        viewModel.globalError.distinctUntilChanged().observe(this) {
            if (it is GlobalError.Show) {
                if (it.canCancelDialog) {
                    navController.navigateDeepLinkWithoutOptions(
                        "dialog-dest",
                        "com.grand.ezkorone.global.error.dialog.cancellable",
                        if (it.errorMsg.isNullOrEmpty()) getString(R.string.something_went_wrong) else it.errorMsg,
                        true.toString()
                    )
                }else {
                    navController.navigateDeepLinkWithoutOptions(
                        "dialog-dest",
                        "com.grand.ezkorone.global.error.dialog",
                        if (it.errorMsg.isNullOrEmpty()) getString(R.string.something_went_wrong) else it.errorMsg,
                    )
                }
            }
        }
    }

}

package com.grand.ezkorone.presentation.internalNavigation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.NavBottomNavDirections
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.inflateGraph
import com.grand.ezkorone.core.extensions.inflateMenuViaRes
import com.grand.ezkorone.databinding.FragmentBottomNavBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavFragment : MABaseFragment<FragmentBottomNavBinding>() {

    private val destinationsShowToolbar = setOf(
        -1,
    )

    private val menuResOfBottomNav = R.menu.menu_bottom_nav

    private val navigationResOfGraph: Int = R.navigation.nav_bottom_nav

    private val onDestinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        run {
            binding?.root?.post {
                activityViewModel.showToolbar.value = destination.id in destinationsShowToolbar
                destination.label?.toString().orEmpty().also {
                    if (it.isNotEmpty()) {
                        activityViewModel.titleToolbar.value = it
                    }
                }

                getSelectedItemIdFromCurrentDestinationId(destination.id)?.also {
                    if (binding?.bottomNavigationView?.selectedItemId != it) {
                        binding?.bottomNavigationView?.selectedItemId = it
                    }
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_bottom_nav

    private fun adjustBottomNavigationView(navController: NavController) {
        binding?.bottomNavigationView?.inflateMenuViaRes(menuResOfBottomNav)
        getSelectedItemIdFromCurrentDestinationId(navController.currentDestination?.id)?.also {
            binding?.bottomNavigationView?.selectedItemId = it
        }
        binding?.bottomNavigationView?.setOnItemSelectedListener {
            onItemSelectedListener(navController, it)

            true // change to be selected instead of ignoring the click.
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.bottomNavNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.inflateGraph(navigationResOfGraph)

        navController.removeOnDestinationChangedListener(onDestinationChangedListener)
        navController.addOnDestinationChangedListener(onDestinationChangedListener)

        adjustBottomNavigationView(navController)
    }

    private fun getSelectedItemIdFromCurrentDestinationId(destinationId: Int?): Int? {
        return when (destinationId) {
            R.id.dest_home -> R.id.action_home
            R.id.dest_salah -> R.id.action_pray
            R.id.dest_qibla -> R.id.action_qibla
            R.id.dest_taspeh -> R.id.action_taspeh
            else -> null
        }
    }

    private fun onItemSelectedListener(navController: NavController, menuItem: MenuItem) {
        val action = when (menuItem.itemId) {
            R.id.action_home -> NavBottomNavDirections.actionGlobalDestHome()
            R.id.action_pray -> NavBottomNavDirections.actionGlobalDestSalah()
            R.id.action_qibla -> NavBottomNavDirections.actionGlobalDestQibla()
            R.id.action_taspeh -> NavBottomNavDirections.actionGlobalDestTaspeh()
            else -> return
        }

        navController.navigate(action)
    }

}

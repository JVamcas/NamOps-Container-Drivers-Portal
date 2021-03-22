package com.pet001kambala.namopscontainers

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.pet001kambala.namopscontainers.databinding.ActivityMainBinding
import com.pet001kambala.namopscontainers.databinding.NavHeaderMainBinding
import com.pet001kambala.namopscontainers.ui.trip.TripViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var tripModel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        tripModel = ViewModelProvider(this).get(TripViewModel::class.java)

        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration =
            AppBarConfiguration(
                setOf(R.id.homeFragment), binding.drawerLayout
            )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(this)
        val navBinding: NavHeaderMainBinding =
            NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
//        accountModel.currentAccount.observe(this) {
//            navBinding.user = it
//            account = it
//            it?.let {
//                refreshMainActivity()
//            }
//        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        val curDest = navController.currentDestination?.id


        when (item.itemId) {
            R.id.nav_home -> {
                if (curDest != R.id.homeFragment)
                    navController.navigate(R.id.action_global_homeFragment)
            }
            R.id.nav_my_truck -> {
                if (curDest != R.id.updateTruckDetailsFragment)
                    navController.navigate(R.id.action_global_updateTruckDetailsFragment)
            }
        }
        return false
    }
}
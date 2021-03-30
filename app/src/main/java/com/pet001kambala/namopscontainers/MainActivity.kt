package com.pet001kambala.namopscontainers

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.pet001kambala.namopscontainers.ui.account.AccountViewModel
import com.pet001kambala.namopscontainers.ui.trip.TripViewModel
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var tripModel: TripViewModel
    private lateinit var accountModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        tripModel = ViewModelProvider(this).get(TripViewModel::class.java)
        accountModel = ViewModelProvider(this).get(AccountViewModel::class.java)

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
        accountModel.currentDriver.observe(this) {
            navBinding.user = it
        }
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
            R.id.about_developer -> if (curDest != R.id.aboutDeveloperFragment)
                navController.navigate(R.id.action_global_aboutDeveloperFragment)
        }
        return false
    }

    @ExperimentalCoroutinesApi
    fun onSignOut(view: View) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        accountModel.signOut().observe(this) { results ->
            if (results is Results.Success<*>)
                Toast.makeText(this, "Signed Out.", Toast.LENGTH_SHORT).show()
        }
    }
}
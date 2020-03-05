package com.rwoods.thecomicsoracle.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rwoods.thecomicsoracle.R
import kotlinx.android.synthetic.main.activity_comics_oracle_main.*


class ComicsOracleMainActivity : AppCompatActivity() {
    var sharedPrefs: SharedPreferences? = null

    var isRestarted = false
        private set


    override fun onStart() {
        if (!isRestarted) {
            val editor = sharedPrefs?.edit()
            editor?.clear()
            editor?.apply()
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comics_oracle_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "The Comics Oracle"
        setupNavigation()
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_comics_oracle_main, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun setupNavigation() {
        val navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(
                topLevelDestinationIds = setOf (
                        R.id.characterSearchFragment,
                        R.id.videoSearchFragment
                )
        )

        /*navController.addOnDestinationChangedListener {
            controller, destination, arguments ->
            toolbar.title = navController.currentDestination?.label
        }*/

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.navHostFragment).navigateUp()
}

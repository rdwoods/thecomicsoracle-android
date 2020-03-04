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
        //setUpActionBar()
        setupNavigation()
    }

    private fun setupViewPager() {

        val comicsOracleSectionPagerAdapter = ComicsOracleSectionPagerAdapter(supportFragmentManager)

        val characterSearchFragment: CharacterSearchFragment = CharacterSearchFragment.newInstance()
        val videoSearchFragment: VideoSearchFragment = VideoSearchFragment.newInstance()
        //val favoriteCharacterFragment: FavoriteCharactersFragment = FavoriteCharactersFragment.newInstance()

        comicsOracleSectionPagerAdapter.addFragment(characterSearchFragment, getString(R.string.title_character_search))
        comicsOracleSectionPagerAdapter.addFragment(videoSearchFragment, getString(R.string.title_video_search))
        //comicsOracleSectionPagerAdapter.addFragment(favoriteCharacterFragment, getString(R.string.title_favorites))

        viewPager.adapter = comicsOracleSectionPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_comics_oracle_main, menu)
        return true
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.navHostFragment).navigateUp()
}

package com.rwoods.thecomicsoracle.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.ui.base.ComicsOracleSectionPagerAdapter
import com.rwoods.thecomicsoracle.ui.search.CharacterSearchFragment
import com.rwoods.thecomicsoracle.ui.video.VideoSearchFragment
import com.rwoods.thecomicsoracle.util.Constants
import kotlinx.android.synthetic.main.activity_comics_oracle_main.*
import java.util.*

class ComicsOracleMainActivity : AppCompatActivity() {

    var sharedPrefs: SharedPreferences? = null


    var isRestarted = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comics_oracle_main)

        setSupportActionBar(toolbar)

        sharedPrefs = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)

        setupViewPager()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        if (!isRestarted) {
            val editor = sharedPrefs?.edit()
            editor?.clear()
            editor?.apply()
        }
        super.onStart()
    }


    companion object {

        /*Comparator for sorting the list by Merchant Name in asc order*/
        var CharacterComparator: Comparator<ComicCharacter> = Comparator { comicCharacter1, comicCharacter2 ->
            val char1 = comicCharacter1.name?.toLowerCase(Locale.getDefault()) as String
            val char2 = comicCharacter2.name?.toLowerCase(Locale.getDefault()) as String

            char1.compareTo(char2)
        }
    }
}

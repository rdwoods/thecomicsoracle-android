package com.rwoods.thecomicsoracle.activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.fragment.CharacterSearchFragment
import com.rwoods.thecomicsoracle.fragment.FavoriteCharactersFragment
import com.rwoods.thecomicsoracle.fragment.VideoSearchFragment
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.util.Constants
import io.realm.Realm
import java.util.*

class ComicsOracleMainActivity : AppCompatActivity() {

    var sharedPrefs: SharedPreferences? = null

    var realm: Realm? = null

    var isRestarted = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comics_oracle_main)

        sharedPrefs = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance()

        // Set up the toolbar and action bar.
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Create the com.rwoods.thecomicsoracle.adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        val mComicsOracleSectionPagerAdapter = ComicsOracleSectionPagerAdapter(supportFragmentManager, this)

        // Set up the ViewPager with the sections com.rwoods.thecomicsoracle.adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        val mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager.adapter = mComicsOracleSectionPagerAdapter
            //mViewPager.setOffscreenPageLimit(2);

        /*ArrayList<Drawable> tabIcons = new ArrayList<>();
        tabIcons.add(ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_search_white_18dp));
        tabIcons.add(ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_video_library_white_18dp));
        tabIcons.add(ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_thumb_up_white_18dp));*/

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        //IconTextTabLayout tabLayout = (IconTextTabLayout) findViewById(R.id.tabs);
        //tabLayout.setTabIcons(tabIcons);
        tabLayout.setupWithViewPager(mViewPager)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_comics_oracle_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class ComicsOracleSectionPagerAdapter internal constructor(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            when (position) {
                0 -> return CharacterSearchFragment.newInstance()

                1 -> return VideoSearchFragment.newInstance()

                2 -> return FavoriteCharactersFragment.newInstance()
            }
            return null
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            // Generate title based on item position
            return getTabText(position)
        }


        internal fun getTabText(position: Int): CharSequence? {
            val l = Locale.getDefault()

            when (position) {
                0 -> return getString(R.string.title_character_search)
                1 -> return getString(R.string.title_video_search)
                2 -> return getString(R.string.title_favorites)
            }
            return null
        }

        internal fun getTabIcon(position: Int): Drawable? {
            when (position) {
                0 -> return ContextCompat.getDrawable(this@ComicsOracleMainActivity, R.drawable.ic_search_white_18dp)
                1 -> return ContextCompat.getDrawable(this@ComicsOracleMainActivity, R.drawable.ic_video_library_white_18dp)
                2 -> return ContextCompat.getDrawable(this@ComicsOracleMainActivity, R.drawable.ic_thumb_up_white_18dp)
            }
            return null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        if (!isRestarted) {
            val editor = sharedPrefs!!.edit()
            editor.clear()
            editor.apply()
        }
        super.onStart()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        isRestarted = true
        super.onRestart()
    }

    companion object {

        /*Comparator for sorting the list by Merchant Name in asc order*/
        var CharacterComparator: Comparator<ComicCharacter> = Comparator { comicCharacter1, comicCharacter2 ->
            val char1 = comicCharacter1.name?.toLowerCase() as String
            val char2 = comicCharacter2.name?.toLowerCase() as String

            char1.compareTo(char2)
        }
    }
}

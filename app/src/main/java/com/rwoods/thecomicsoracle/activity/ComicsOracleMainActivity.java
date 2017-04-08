package com.rwoods.thecomicsoracle.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.rwoods.thecomicsoracle.R;
import com.rwoods.thecomicsoracle.entity.ComicCharacter;
import com.rwoods.thecomicsoracle.fragment.CharacterSearchFragment;
import com.rwoods.thecomicsoracle.fragment.FavoriteCharactersFragment;
import com.rwoods.thecomicsoracle.fragment.VideoSearchFragment;
import com.rwoods.thecomicsoracle.util.Constants;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import java.util.Comparator;
import java.util.Locale;

public class ComicsOracleMainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private Realm realm;

    private boolean isRestarted = false;

    /*Comparator for sorting the list by Merchant Name in asc order*/
    public static Comparator<ComicCharacter> CharacterComparator = new Comparator<ComicCharacter>() {

        public int compare(ComicCharacter comicCharacter1, ComicCharacter comicCharacter2) {
            String char1 = comicCharacter1.getName().toLowerCase();
            String char2 = comicCharacter2.getName().toLowerCase();

            return char1.compareTo(char2);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics_oracle_main);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

        // Set up the toolbar and action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

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
        ComicsOracleSectionPagerAdapter mComicsOracleSectionPagerAdapter = new ComicsOracleSectionPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections com.rwoods.thecomicsoracle.adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mComicsOracleSectionPagerAdapter);
            //mViewPager.setOffscreenPageLimit(2);
        }

        /*ArrayList<Drawable> tabIcons = new ArrayList<>();
        tabIcons.add(ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_search_white_18dp));
        tabIcons.add(ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_video_library_white_18dp));
        tabIcons.add(ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_thumb_up_white_18dp));*/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //IconTextTabLayout tabLayout = (IconTextTabLayout) findViewById(R.id.tabs);
        //tabLayout.setTabIcons(tabIcons);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comics_oracle_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class ComicsOracleSectionPagerAdapter extends FragmentPagerAdapter {

        ComicsOracleSectionPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return CharacterSearchFragment.newInstance();

                case 1:
                    return VideoSearchFragment.newInstance();

                case 2:
                    return FavoriteCharactersFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return getTabText(position);
        }


        CharSequence getTabText(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return getString(R.string.title_character_search);
                case 1:
                    return getString(R.string.title_video_search);
                case 2:
                    return getString(R.string.title_favorites);
            }
            return null;
        }

        Drawable getTabIcon(int position) {
            switch (position) {
                case 0:
                    return ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_search_white_18dp);
                case 1:
                    return ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_video_library_white_18dp);
                case 2:
                    return ContextCompat.getDrawable(ComicsOracleMainActivity.this, R.drawable.ic_thumb_up_white_18dp);
            }
            return null;
        }
    }


    public SharedPreferences getSharedPrefs() {
        return sharedPreferences;
    }

    public void setSharedPrefs(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        if (!isRestarted){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        isRestarted = true;
        super.onRestart();
    }

    public boolean isRestarted() {
        return isRestarted;
    }
}

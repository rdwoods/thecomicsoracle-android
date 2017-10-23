package com.rwoods.thecomicsoracle.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.viewmodel.CharacterSearchFragmentViewModel
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterSearchFragment : Fragment() {

    private var mCharacterRecyclerView: RecyclerView? = null

    private var mProgressBar: ProgressBar? = null
    private var mCharacterSearchView: SearchView? = null

    private var savedSearchTerm: String? = null

    val fragmentName: String
        get() = FRAGMENT_NAME


    private var mViewModel: CharacterSearchFragmentViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val fragmentName = arguments.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        mCharacterSearchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as android.support.v7.widget.SearchView

        if (mCharacterSearchView != null) {

            if (!savedSearchTerm!!.isEmpty()) {
                mCharacterSearchView!!.setQuery(savedSearchTerm, false)
            }

            mCharacterSearchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))

            mCharacterSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchText: String): Boolean {

                    clearPreviousList()

                    showProgress(true)

                    mViewModel?.getCharactersFromRest(searchText.trim { it <= ' ' })

                    mCharacterSearchView!!.clearFocus()

                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {

                    if (searchText.trim { it <= ' ' }.isEmpty()) {

                        clearPreviousList()

                        mViewModel?.setCharacterList(mViewModel?.getCharacterAdapter()?.mCharacterList as ArrayList<ComicCharacter>)
                        mViewModel?.setAdapterOnClick()

                    }

                    return false
                }
            })

            mCharacterSearchView!!.setOnCloseListener {
                clearPreviousList()

                false
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_character, container, false)

        mViewModel = CharacterSearchFragmentViewModel(this)

        //Your RecyclerView
        mCharacterRecyclerView = rootView.findViewById(R.id.character_recycler_view) as RecyclerView
        mCharacterRecyclerView!!.setHasFixedSize(true)
        mCharacterRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mCharacterRecyclerView!!.visibility = View.GONE

        mProgressBar = rootView.findViewById(R.id.search_character_progress) as ProgressBar

        mViewModel!!.setUp()

        savedSearchTerm = mViewModel?.getSavedSearchTerm()

        restorePreviousSearchResults()

        return rootView
    }

    private fun restorePreviousSearchResults() {
        val savedData = mViewModel?.getSavedData()

        try {
            if (!savedData!!.isEmpty()) {
                mViewModel?.setCharacterList(mViewModel!!.jsonAdapter!!.fromJson(savedData) as ArrayList<ComicCharacter>)
                mViewModel?.setAdapterOnClick()
                mCharacterRecyclerView!!.adapter = mViewModel?.mComicCharacterAdapter!!
                mCharacterRecyclerView!!.adapter!!.notifyDataSetChanged()
                mCharacterRecyclerView!!.visibility = View.VISIBLE
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun displayCharactersFromRest(comicCharacters: ArrayList<ComicCharacter>?) {
        mViewModel?.setCharacterList(comicCharacters!!)
        mViewModel?.setAdapterOnClick()

        mCharacterRecyclerView!!.adapter = mViewModel?.mComicCharacterAdapter!!

        mViewModel?.clearSearchResultsPreferences()

        try {
            val savedSearchTerm = mCharacterSearchView!!.query.toString()
            val savedData = mViewModel!!.getSavedData()

            mViewModel?.setSavedSearchResults(savedSearchTerm, savedData!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }


        showProgress(false)
        mCharacterRecyclerView!!.visibility = View.VISIBLE

    }

    private fun clearPreviousList() {
        mViewModel?.getCharacterAdapter()?.clear()
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        mProgressBar!!.visibility = if (show) View.VISIBLE else View.GONE
        mProgressBar!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mProgressBar!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }


    override fun onDetach() {
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val FRAGMENT_NAME = "character"

        private val LOGGER = LoggerFactory.getLogger(CharacterSearchFragment::class.java)

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment MadSkilzByCharacterFragment.
         */
        fun newInstance(): CharacterSearchFragment {
            val fragment = CharacterSearchFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun displayCharacterSearchNotRetrieved() {
        Toast.makeText(activity, "Search of characters failed.", Toast.LENGTH_LONG).show()
    }

    fun displayCharacterSearchFailed() {
        mCharacterRecyclerView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.VISIBLE
        activity.runOnUiThread {
            Toast.makeText(activity, "Could not get character list", Toast.LENGTH_LONG).show()

            showProgress(false)
            mCharacterRecyclerView!!.visibility = View.VISIBLE
        }
    }
}
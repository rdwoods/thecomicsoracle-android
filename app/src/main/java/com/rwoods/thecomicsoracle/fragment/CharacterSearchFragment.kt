package com.rwoods.thecomicsoracle.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.SearchManager
import android.arch.lifecycle.Observer
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
import org.slf4j.LoggerFactory
import java.io.IOException
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.util.Constants
import java.nio.charset.StandardCharsets


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterSearchFragment : Fragment() {

    private var characterRecyclerView: RecyclerView? = null

    private var characterAdapter: ComicCharacterAdapter? = null

    private var mProgressBar: ProgressBar? = null
    private var characterSearchView: SearchView? = null

    private var savedSearchTerm: String? = null
    private var comicCharacterObserver: Observer<ArrayList<ComicCharacter>>? = null


    val fragmentName: String
        get() = FRAGMENT_NAME


    private var viewModel: CharacterSearchFragmentViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val fragmentName = arguments?.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        characterSearchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as android.support.v7.widget.SearchView

        if (characterSearchView != null) {

            if (!savedSearchTerm!!.isEmpty()) {
                characterSearchView!!.setQuery(savedSearchTerm, false)
            }

            characterSearchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

            characterSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchText: String): Boolean {

                    clearPreviousList()

                    showProgress(true)

                    viewModel?.getCharactersFromRest(searchText.trim { it <= ' ' })

                    characterSearchView!!.clearFocus()

                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {

                    if (searchText.trim { it <= ' ' }.isEmpty()) {

                        clearPreviousList()
/*
                        viewModel?.setCharacterList(viewModel?.getCharacterAdapter()?.mCharacterList as ArrayList<ComicCharacter>)
                        setAdapterOnClick()*/

                    }

                    return false
                }
            })

            characterSearchView!!.setOnCloseListener {
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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_character, container, false)

        viewModel = ViewModelProviders.of(this).get(CharacterSearchFragmentViewModel::class.java)
        viewModel?.comicCharacters?.observe(this, Observer { comicCharacters ->

            characterRecyclerView!!.adapter = characterAdapter;

            viewModel?.clearSearchResultsPreferences()

            try {
                val savedSearchTerm = characterSearchView!!.query.toString()
                val savedData = viewModel!!.getSavedData()

                viewModel?.setSavedSearchResults(savedSearchTerm, savedData!!)

            } catch (e: Exception) {
                e.printStackTrace()
            }


            showProgress(false)
            characterRecyclerView!!.visibility = View.VISIBLE
        })

        //Your RecyclerView
        characterRecyclerView = rootView.findViewById(R.id.character_recycler_view) as RecyclerView
        characterRecyclerView!!.setHasFixedSize(true)
        characterRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        characterRecyclerView!!.visibility = View.GONE

        mProgressBar = rootView.findViewById(R.id.search_character_progress) as ProgressBar

        viewModel!!.setUp()

        savedSearchTerm = viewModel?.getSavedSearchTerm()

        restorePreviousSearchResults()

        return rootView
    }

    private fun restorePreviousSearchResults() {
        val savedData = viewModel?.getSavedData()

        try {
            if (!savedData!!.isEmpty()) {
                setAdapterOnClick()
                characterRecyclerView!!.adapter = characterAdapter
                characterRecyclerView!!.adapter!!.notifyDataSetChanged()
                characterRecyclerView!!.visibility = View.VISIBLE
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setAdapterOnClick() {
        characterAdapter!!.setOnItemClickListener(object: ComicCharacterAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //val intent = Intent(activity, CharacterDescriptionActivity::class.java)
                val intent = Intent(context, CharacterDescriptionWebViewActivity::class.java)
                val bundle = Bundle()
                var descr: String? = characterAdapter!!.characterList[position].description

                if (descr == null){
                    descr = ""
                }

                val byte: ByteArray? = descr.toByteArray(StandardCharsets.UTF_8)
                bundle.putByteArray(Constants.CHARACTER, byte!!)
                intent.putExtras(bundle)

                startActivity(intent)
            }
        })
    }


    fun displayCharactersFromRest(comicCharacters: ArrayList<ComicCharacter>?) {
        setAdapterOnClick()

        characterRecyclerView!!.adapter = characterAdapter

        viewModel?.clearSearchResultsPreferences()

        try {
            val savedSearchTerm = characterSearchView!!.query.toString()
            val savedData = viewModel!!.getSavedData()

            viewModel?.setSavedSearchResults(savedSearchTerm, savedData!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }


        showProgress(false)
        characterRecyclerView!!.visibility = View.VISIBLE

    }

    private fun clearPreviousList() {
        characterAdapter!!.clear()
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
        characterRecyclerView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.VISIBLE
        activity?.runOnUiThread {
            Toast.makeText(activity, "Could not get character list", Toast.LENGTH_LONG).show()

            showProgress(false)
            characterRecyclerView!!.visibility = View.VISIBLE
        }
    }
}
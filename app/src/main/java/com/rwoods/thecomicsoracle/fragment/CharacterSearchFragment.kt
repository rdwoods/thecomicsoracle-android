package com.rwoods.thecomicsoracle.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast

import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionActivity
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.model.ComicCharacterResponse
import com.rwoods.thecomicsoracle.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.IOException
import java.lang.reflect.Type
import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterSearchFragment : Fragment() {

    private var mComicCharacterAdapter: ComicCharacterAdapter? = null

    private var mCharacterRecyclerView: RecyclerView? = null
    private var asmCharacterRecyclerView: RecyclerView? = null

    private var mComicCharacterList: ArrayList<ComicCharacter>? = null
    private var mSearchedComicCharacterList: ArrayList<ComicCharacter>? = null

    private var mProgressBar: ProgressBar? = null
    private var mCharacterSearchView: SearchView? = null

    private var appSharedPrefs: SharedPreferences? = null

    private var savedSearchTerm: String? = null

    private var jsonAdapter: JsonAdapter<List<ComicCharacter>>? = null
    private var moshi: Moshi? = null

    val fragmentName: String
        get() = FRAGMENT_NAME


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = moshi!!.adapter<List<ComicCharacter>>(type)

        appSharedPrefs = activity.getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)


        savedSearchTerm = appSharedPrefs!!.getString(getString(R.string.saved_character_search_term), "")

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

                    getCharactersFromRest(searchText.trim { it <= ' ' })

                    mCharacterSearchView!!.clearFocus()

                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {

                    if (searchText.trim { it <= ' ' }.isEmpty()) {

                        clearPreviousList()

                        mComicCharacterAdapter = ComicCharacterAdapter(activity, mSearchedComicCharacterList!!)

                        setOnClickListener()
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

        //Your RecyclerView
        mCharacterRecyclerView = rootView.findViewById(R.id.character_recycler_view) as RecyclerView
        mCharacterRecyclerView!!.setHasFixedSize(true)
        mCharacterRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mCharacterRecyclerView!!.visibility = View.GONE

        mProgressBar = rootView.findViewById(R.id.search_character_progress) as ProgressBar

        mComicCharacterList = ArrayList<ComicCharacter>()
        mSearchedComicCharacterList = ArrayList<ComicCharacter>()

        restorePreviousSearchResults()

        return rootView
    }

    private fun restorePreviousSearchResults() {
        val savedData = appSharedPrefs!!.getString(getString(R.string.saved_character_results), "")

        try {
            if (!savedData!!.isEmpty()) {
                mComicCharacterList = jsonAdapter!!.fromJson(savedData) as ArrayList<ComicCharacter>
                mComicCharacterAdapter = ComicCharacterAdapter(activity, mComicCharacterList!!)
                mCharacterRecyclerView!!.adapter = mComicCharacterAdapter
                mComicCharacterAdapter!!.notifyDataSetChanged()
                mCharacterRecyclerView!!.visibility = View.VISIBLE
                setOnClickListener()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getCharactersFromRest(searchText: String) {
        mCharacterRecyclerView!!.visibility = View.GONE
        showProgress(true)

        val filteredCharacterName = "name:" + searchText
        val characterSearchCall = ComicsOracleRetrofitApiRestClient.apiClient?.getCharacterByName(filteredCharacterName)

        characterSearchCall?.enqueue(object : Callback<ComicCharacterResponse> {

            override fun onResponse(call: Call<ComicCharacterResponse>, response: Response<ComicCharacterResponse>) {

                val comicCharacters = response.body()?.comicCharacters as? ArrayList<ComicCharacter>

                if (comicCharacters == null) {
                    //View rootView = getView().findViewById(R.id.character_recycler_view).getRootView();
                    Toast.makeText(context, "Search of characters failed.", Toast.LENGTH_LONG).show()
                    return
                }

                mComicCharacterAdapter = ComicCharacterAdapter(activity, comicCharacters)

                mCharacterRecyclerView!!.adapter = mComicCharacterAdapter

                appSharedPrefs!!
                        .edit()
                        .remove(getString(R.string.saved_character_search_term))
                        .remove(getString(R.string.saved_character_results))
                        .apply()

                try {
                    val savedSearchTerm = mCharacterSearchView!!.query.toString()
                    val savedData = jsonAdapter!!.toJson(mComicCharacterAdapter!!.characterList)
                    appSharedPrefs!!
                            .edit()
                            .putString(getString(R.string.saved_character_search_term), savedSearchTerm)
                            .putString(getString(R.string.saved_character_results), savedData)
                            .apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                showProgress(false)
                mCharacterRecyclerView!!.visibility = View.VISIBLE

                setOnClickListener()
            }

            override fun onFailure(call: Call<ComicCharacterResponse>, throwable: Throwable) {
                mCharacterRecyclerView!!.visibility = View.GONE
                mProgressBar!!.visibility = View.VISIBLE
                activity.runOnUiThread {
                    Toast.makeText(context, "Could not get character list", Toast.LENGTH_LONG).show()

                    showProgress(false)
                    mCharacterRecyclerView!!.visibility = View.VISIBLE
                }
            }
        })
    }


    private fun clearPreviousList() {
        mComicCharacterList!!.clear()
    }


    private fun setOnClickListener() {

        mComicCharacterAdapter!!.setOnItemClickListener(object: ComicCharacterAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //val intent = Intent(activity, CharacterDescriptionActivity::class.java)
                val intent = Intent(activity, CharacterDescriptionWebViewActivity::class.java)
                val bundle = Bundle()
                val comicCharacterJsonAdapter = moshi!!.adapter(ComicCharacter::class.java)
                val json = comicCharacterJsonAdapter.toJson(mComicCharacterAdapter!!.characterList[position])
                //bundle.putString(Constants.CHARACTER, json);
                var byte: ByteArray? = mComicCharacterAdapter!!.characterList[position].description?.toByteArray(StandardCharsets.UTF_8)
                bundle.putByteArray(Constants.CHARACTER, byte!!)
                intent.putExtras(bundle)

                startActivity(intent)
            }
        })
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

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
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
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val FRAGMENT_NAME = "character"

        private val LOGGER = LoggerFactory.getLogger(CharacterSearchFragment::class.java)

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment MadSkilzByCharacterFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): CharacterSearchFragment {
            val fragment = CharacterSearchFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
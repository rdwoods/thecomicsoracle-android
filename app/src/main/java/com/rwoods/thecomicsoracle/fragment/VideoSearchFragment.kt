package com.rwoods.thecomicsoracle.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
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
import com.rwoods.thecomicsoracle.model.Video
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.viewmodel.VideoSearchFragmentViewModel
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [VideoSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoSearchFragment : Fragment() {

    private var mVideoRecyclerView: RecyclerView? = null

    private var mProgressBar: ProgressBar? = null
    private var mVideoSearchView: SearchView? = null

    private var mComicsOracleRepo: ComicsOracleRepository? = null

    private var savedSearchTerm: String? = null

    val fragmentName: String
        get() = FRAGMENT_NAME


    private var mViewModel: VideoSearchFragmentViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mComicsOracleRepo = ComicsOracleRepository(context)
        savedSearchTerm = mComicsOracleRepo!!.helper.savedSearchTerm

        if (arguments != null) {
            val fragmentName = arguments.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        mVideoSearchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as android.support.v7.widget.SearchView

        if (mVideoSearchView != null) {

            if (!savedSearchTerm!!.isEmpty()) {
                mVideoSearchView!!.setQuery(savedSearchTerm, false)
            }

            mVideoSearchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))

            mVideoSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchText: String): Boolean {

                    clearPreviousList()

                    showProgress(true)

                    mViewModel?.getVideosFromRest(searchText.trim { it <= ' ' })

                    mVideoSearchView!!.clearFocus()

                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {

                    if (searchText.trim { it <= ' ' }.isEmpty()) {

                        clearPreviousList()

                        mViewModel?.setVideoAdapter(context, mViewModel?.mSearchedVideoList!!)
                        mViewModel?.setAdapterOnClick()

                    }

                    return false
                }
            })

            mVideoSearchView!!.setOnCloseListener {
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

        mViewModel = VideoSearchFragmentViewModel(this)

        //Your RecyclerView
        mVideoRecyclerView = rootView.findViewById(R.id.character_recycler_view) as RecyclerView
        mVideoRecyclerView!!.setHasFixedSize(true)
        mVideoRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mVideoRecyclerView!!.visibility = View.GONE

        mProgressBar = rootView.findViewById(R.id.search_character_progress) as ProgressBar

        mViewModel!!.setUp()

        restorePreviousSearchResults()

        return rootView
    }

    private fun restorePreviousSearchResults() {
        val savedData = mComicsOracleRepo?.helper?.savedSearchTerm

        try {
            if (!savedData!!.isEmpty()) {
                mViewModel?.setVideoList(mViewModel!!.jsonAdapter!!.fromJson(savedData) as ArrayList<Video>)
                mViewModel?.setVideoAdapter(context, mViewModel?.mVideoList!!)
                mViewModel?.setAdapterOnClick()
                mVideoRecyclerView!!.adapter = mViewModel?.mVideoAdapter!!
                mViewModel?.mVideoAdapter!!.notifyDataSetChanged()
                mVideoRecyclerView!!.visibility = View.VISIBLE
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun displayVideosFromRest(comicVideos: ArrayList<Video>?) {
        mViewModel?.setVideoAdapter(context, comicVideos!!)
        mViewModel?.setAdapterOnClick()

        mVideoRecyclerView!!.adapter = mViewModel?.mVideoAdapter!!

        mComicsOracleRepo?.helper?.clearSearchResultsPreferences()

        try {
            val savedSearchTerm = mVideoSearchView!!.query.toString()
            val savedData = mViewModel!!.getSavedData()

            mComicsOracleRepo?.helper?.setSavedSearchResults(savedSearchTerm, savedData!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }


        showProgress(false)
        mVideoRecyclerView!!.visibility = View.VISIBLE

    }

    private fun clearPreviousList() {
        mViewModel?.mVideoList!!.clear()
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

    fun displayVideoSearchNotRetrieved() {
        Toast.makeText(context, "Search of characters failed.", Toast.LENGTH_LONG).show()
    }

    fun displayVideoSearchFailed() {
        mVideoRecyclerView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.VISIBLE
        activity.runOnUiThread {
            Toast.makeText(context, "Could not get video list", Toast.LENGTH_LONG).show()

            showProgress(false)
            mVideoRecyclerView!!.visibility = View.VISIBLE
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val FRAGMENT_NAME = "character"

        private val LOGGER = LoggerFactory.getLogger(VideoSearchFragment::class.java)

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment MadSkilzByVideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): VideoSearchFragment {
            val fragment = VideoSearchFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
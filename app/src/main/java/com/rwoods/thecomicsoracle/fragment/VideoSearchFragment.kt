package com.rwoods.thecomicsoracle.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
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
import com.rwoods.thecomicsoracle.activity.VideoViewActivity
import com.rwoods.thecomicsoracle.adapter.VideoAdapter
import com.rwoods.thecomicsoracle.model.Video
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.util.Constants
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [VideoSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoSearchFragment : Fragment() {

    private var videoRecyclerView: RecyclerView? = null

    private var videoAdapter: VideoAdapter? = null

    private var mProgressBar: ProgressBar? = null
    private var videoSearchView: SearchView? = null

    private var mComicsOracleRepo: ComicsOracleRepository? = null

    private var savedSearchTerm: String? = null

    val fragmentName: String
        get() = FRAGMENT_NAME


    private var viewModel: VideoSearchFragmentViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //savedSearchTerm = viewModel.helper.savedSearchTerm

        if (arguments != null) {
            val fragmentName = arguments!!.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        videoSearchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as android.support.v7.widget.SearchView

        if (videoSearchView != null) {

            if (savedSearchTerm != null && !savedSearchTerm!!.isEmpty()) {
                videoSearchView!!.setQuery(savedSearchTerm, false)
            }

            videoSearchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            videoSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchText: String): Boolean {

                    videoAdapter!!.clearVideoSearchList()

                    showProgress(true)

                    viewModel?.getVideosFromRest(searchText.trim { it <= ' ' })

                    videoSearchView!!.clearFocus()

                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {
                    return false
                }
            })

            videoSearchView!!.setOnCloseListener {
                clearPreviousList()

                false
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_character, container, false)

        viewModel = ViewModelProviders.of(this).get(VideoSearchFragmentViewModel::class.java)

        videoAdapter = VideoAdapter(this.context!!)

        videoRecyclerView!!.adapter = videoAdapter

        videoRecyclerView = rootView.findViewById(R.id.character_recycler_view) as RecyclerView
        videoRecyclerView!!.setHasFixedSize(true)
        videoRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        videoRecyclerView!!.visibility = View.GONE

        mProgressBar = rootView.findViewById(R.id.search_character_progress) as ProgressBar

        viewModel!!.setUp()

        viewModel?.videos?.observe(this, Observer { comicsVideos ->

            showProgress(false)
            videoRecyclerView!!.visibility = View.VISIBLE

            if (comicsVideos != null) {
                videoAdapter!!.populateAdapter(comicsVideos)
                setAdapterOnClick()
            } else {
                displayVideoSearchFailed()
            }
        })

        restorePreviousSearchResults()

        return rootView
    }

    private fun setAdapterOnClick(){
        videoAdapter!!.setOnItemClickListener(object: VideoAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //val intent = Intent(activity, CharacterDescriptionActivity::class.java)
                val intent = Intent(context, VideoViewActivity::class.java)
                val bundle = Bundle()
                var url: String? = videoAdapter!!.searchedVideos.get(position).highUrl

                if (url == null){
                    url = ""
                }

                bundle.putString(Constants.VIDEO_URL, url)
                intent.putExtras(bundle)

                startActivity(intent)
            }
        })
    }

    private fun restorePreviousSearchResults() {
        val savedData = mComicsOracleRepo?.helper?.savedSearchTerm ?: return

        try {
            if (!savedData.isEmpty()) {
                viewModel?.setVideoList(viewModel!!.jsonAdapter!!.fromJson(savedData) as ArrayList<Video>)
                setAdapterOnClick()
                videoRecyclerView!!.adapter = videoAdapter
                videoRecyclerView!!.adapter!!.notifyDataSetChanged()
                videoRecyclerView!!.visibility = View.VISIBLE
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun displayVideosFromRest(comicVideos: ArrayList<Video>?) {
        setAdapterOnClick()
        videoRecyclerView!!.adapter = videoAdapter
        videoRecyclerView!!.adapter!!.notifyDataSetChanged()

        mComicsOracleRepo?.helper?.clearSearchResultsPreferences()

        try {
            val savedSearchTerm = videoSearchView!!.query.toString()
            val savedData = viewModel!!.getSavedData()

            mComicsOracleRepo?.helper?.setSavedSearchResults(savedSearchTerm, savedData!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }


        showProgress(false)
        videoRecyclerView!!.visibility = View.VISIBLE

    }

    private fun clearPreviousList() {
        viewModel?.videosList!!.clear()
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


    fun displayVideoSearchNotRetrieved() {
        Toast.makeText(context, "Search of characters failed.", Toast.LENGTH_LONG).show()
    }

    fun displayVideoSearchFailed() {
        videoRecyclerView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.VISIBLE
        activity?.runOnUiThread {
            Toast.makeText(context, "Could not get video list", Toast.LENGTH_LONG).show()

            showProgress(false)
            videoRecyclerView!!.visibility = View.VISIBLE
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
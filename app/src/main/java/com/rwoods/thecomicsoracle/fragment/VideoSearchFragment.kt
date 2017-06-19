package com.rwoods.thecomicsoracle.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
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
import com.rwoods.thecomicsoracle.activity.ComicsOracleMainActivity
import com.rwoods.thecomicsoracle.activity.VideoViewActivity
import com.rwoods.thecomicsoracle.adapter.VideoAdapter
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient
import com.rwoods.thecomicsoracle.model.Video
import com.rwoods.thecomicsoracle.model.VideoResponse
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

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoSearchFragment : Fragment() {

    private var mVideoAdapter: VideoAdapter? = null
    private var fragmentName: String? = null

    private var mVideoRecyclerView: RecyclerView? = null

    private var mVideoList: ArrayList<Video>? = null
    private var mSearchedVideoList: ArrayList<Video>? = null

    private var mProgressBar: ProgressBar? = null
    private var mVideoSearchView: SearchView? = null

    private var appSharedPrefs: SharedPreferences? = null

    private var savedSearchTerm: String? = null
    private var jsonAdapter: JsonAdapter<List<Video>>? = null

    fun getFragmentName(): String {
        return FRAGMENT_NAME
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Video::class.java)
        jsonAdapter = moshi.adapter<List<Video>>(type)

        appSharedPrefs = activity.getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)


        savedSearchTerm = appSharedPrefs!!.getString(getString(R.string.saved_video_search_term), "")

        if (arguments != null) {
            fragmentName = arguments.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //MenuItem searchItem = menu.findItem(R.id.action_search);

        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        mVideoSearchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as android.support.v7.widget.SearchView
        //}
        if (mVideoSearchView != null) {

            if (!savedSearchTerm!!.isEmpty()) {
                mVideoSearchView!!.setQuery(savedSearchTerm, false)
            }

            mVideoSearchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))

            mVideoSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchText: String): Boolean {

                    clearPreviousList()

                    getVideosFromRest(searchText)

                    mVideoSearchView!!.clearFocus()

                    return false
                }

                override fun onQueryTextChange(searchText: String): Boolean {

                    if (searchText.isEmpty()) {

                        clearPreviousList()

                        mVideoAdapter = VideoAdapter(activity, mSearchedVideoList as ArrayList<Video>)

                        setOnClickListener()
                    }

                    return false
                }
            })

            mVideoSearchView!!.setOnCloseListener {
                clearPreviousList()

                setOnClickListener()

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
        val rootView = inflater!!.inflate(R.layout.fragment_videos, container, false)

        //Your RecyclerView
        mVideoRecyclerView = rootView.findViewById(R.id.video_recycler_view) as RecyclerView
        mVideoRecyclerView!!.setHasFixedSize(true)
        mVideoRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mVideoRecyclerView!!.visibility = View.GONE

        mProgressBar = rootView.findViewById(R.id.search_video_progress) as ProgressBar

        mVideoList = ArrayList<Video>()
        mSearchedVideoList = ArrayList<Video>()

        restorePreviousSearchResults()

        return rootView
    }

    private fun restorePreviousSearchResults() {
        val savedData = appSharedPrefs!!.getString(activity.getString(R.string.saved_video_results), "")

        try {
            if (!savedData!!.isEmpty()) {
                mVideoList = jsonAdapter!!.fromJson(savedData) as ArrayList<Video>
                mVideoAdapter = VideoAdapter(activity, mVideoList as ArrayList<Video>)
                mVideoRecyclerView!!.adapter = mVideoAdapter
                mVideoAdapter!!.notifyDataSetChanged()
                mVideoRecyclerView!!.visibility = View.VISIBLE
                setOnClickListener()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getVideosFromRest(searchText: String) {
        mVideoRecyclerView!!.visibility = View.GONE
        showProgress(true)

        val filteredVideo = "name:" + searchText
        val videoResponseCall = ComicsOracleRetrofitApiRestClient.apiClient?.getVideoByName(filteredVideo)

        videoResponseCall?.enqueue(object : Callback<VideoResponse> {

            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {

                val videos: ArrayList<Video>?

                videos = response.body()?.videos as ArrayList<Video>

                if (videos == null) {
                    //View rootView = getView().findViewById(R.id.character_recycler_view).getRootView();
                    Toast.makeText(context, "Search of video failed.", Toast.LENGTH_LONG).show()
                    return
                }

                mVideoAdapter = VideoAdapter(activity, videos)

                mVideoRecyclerView!!.adapter = mVideoAdapter

                (activity as ComicsOracleMainActivity).sharedPrefs!!.edit()
                        .remove(getString(R.string.saved_video_search_term))
                        .remove(getString(R.string.saved_video_results))
                        .apply()

                try {
                    val savedSearchTerm = mVideoSearchView!!.query.toString()
                    val savedData = jsonAdapter!!.toJson(mVideoList)

                    (activity as ComicsOracleMainActivity).sharedPrefs!!.edit()
                            .putString(getString(R.string.saved_video_search_term), savedSearchTerm)
                            .putString(getString(R.string.saved_video_results), savedData)
                            .apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                showProgress(false)
                mVideoRecyclerView!!.visibility = View.VISIBLE

                setOnClickListener()
            }

            override fun onFailure(call: Call<VideoResponse>, throwable: Throwable) {
                activity.runOnUiThread {
                    Toast.makeText(context, "Video List Failed.", Toast.LENGTH_LONG).show()

                    showProgress(false)
                    mVideoRecyclerView!!.visibility = View.VISIBLE
                }
            }
        })
    }


    private fun clearPreviousList() {
        mVideoList!!.clear()
    }


    private fun setOnClickListener() {

        mVideoAdapter!!.setOnItemClickListener(object: VideoAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, VideoViewActivity::class.java)
                val bundle = Bundle()
                bundle.putString(Constants.VIDEO_URL, mVideoAdapter!!.videoList[position].highUrl)
                intent.putExtras(bundle) //Put your id to your next Intent

                startActivity(intent)
            }
        })
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            mProgressBar!!.visibility = if (show) View.VISIBLE else View.GONE
            mProgressBar!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mProgressBar!!.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar!!.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val FRAGMENT_NAME = "video"

        private val LOGGER = LoggerFactory.getLogger(CharacterSearchFragment::class.java)

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment MadSkilzByCharacterFragment.
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
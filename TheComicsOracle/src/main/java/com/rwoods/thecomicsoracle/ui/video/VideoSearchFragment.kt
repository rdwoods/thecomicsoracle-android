package com.rwoods.thecomicsoracle.ui.video

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.data.model.Video
import com.rwoods.thecomicsoracle.ui.ComicsOracleMainActivity
import com.rwoods.thecomicsoracle.util.Constants
import kotlinx.android.synthetic.main.fragment_search_results.*
import org.slf4j.LoggerFactory

/**
 * A simple [Fragment] subclass.
 * Use the [VideoSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoSearchFragment : Fragment() {

    private var videoAdapter: VideoAdapter? = null

    private var videoSearchView: SearchView? = null

    private var savedSearchTerm: String? = null

    val fragmentName: String
        get() = FRAGMENT_NAME


    private lateinit var viewModel: VideoSearchFragmentViewModel

    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val fragmentName = arguments?.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(VideoSearchFragmentViewModel::class.java)

        viewModel.progressBarLiveData.observe(this, Observer<Boolean> {
            progressIndicator.visibility = if (it) { View.VISIBLE } else { View.GONE }
        })

        viewModel.videosMutableLiveData.observe(this, Observer<MutableList<Video>> { videos ->
            videos?.apply {
                (recyclerViewResults.adapter as VideoAdapter).populateAdapter(this)

                recyclerViewResults.visibility = View.VISIBLE
            } ?: run {

                activity?.runOnUiThread {
                    Toast.makeText(activity, "Could not get results", Toast.LENGTH_LONG).show()

                    recyclerViewResults.visibility = View.VISIBLE
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()

        inflater.inflate(R.menu.menu_comics_oracle_main, menu)

        searchView = SearchView((context as ComicsOracleMainActivity).supportActionBar?.themedContext ?: context)

        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchText: String): Boolean {

                videoAdapter?.clear()

                viewModel.getVideos(searchText.trim { it <= ' ' })

                searchView.clearFocus()

                return false
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                return false
            }
        })

        searchView.setOnClickListener {view ->  }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoAdapter = VideoAdapter(requireContext())
        recyclerViewResults.setHasFixedSize(true)
        recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewResults.visibility = View.GONE

        videoAdapter?.setOnItemClickListener(object: VideoAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(context, VideoViewActivity::class.java)
                val bundle = Bundle()
                val url: String? = videoAdapter?.searchedVideos?.get(position)?.highUrl


                url?.apply {
                    bundle.putString(Constants.VIDEO_URL, this)
                    intent.putExtras(bundle)

                    startActivity(intent)
                } ?: run {
                    startActivity(intent)
                }
            }
        })

        recyclerViewResults.adapter = videoAdapter
    }

    /*private fun restorePreviousSearchResults() {
        try {
            if (!savedData!!.isEmpty()) {
                setAdapterOnClick()
                recyclerViewResults.adapter = characterAdapter
                recyclerViewResults.adapter!!.notifyDataSetChanged()
                recyclerViewResults.visibility = View.VISIBLE
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/

    /*fun displayVideosFromRest(comicVideos: ArrayList<Video>?) {
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

    }*/

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
package com.rwoods.thecomicsoracle.ui.videos

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.data.model.Video
import com.rwoods.thecomicsoracle.ui.ComicsOracleMainViewModel
import kotlinx.android.synthetic.main.fragment_search_results.*

class VideoSearchFragment : Fragment() {

    private var videoAdapter: VideoAdapter? = null

    private val viewModel: ComicsOracleMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

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
                val videoUrl = videoAdapter?.searchedVideos?.get(position)?.highUrl
                val action = VideoSearchFragmentDirections.actionToVideoView(videoUrl)
                view.findNavController().navigate(action)
            }
        })

        recyclerViewResults.adapter = videoAdapter

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

}
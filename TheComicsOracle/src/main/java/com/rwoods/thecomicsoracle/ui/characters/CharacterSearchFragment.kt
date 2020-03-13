package com.rwoods.thecomicsoracle.ui.characters

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.ui.ComicsOracleMainViewModel
import com.rwoods.thecomicsoracle.ui.description.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.util.Constants
import kotlinx.android.synthetic.main.fragment_search_results.*
import java.nio.charset.StandardCharsets


class CharacterSearchFragment : Fragment() {

    private var characterAdapter: ComicCharacterAdapter? = null

    private var characterSearchView: SearchView? = null

    private var savedSearchTerm: String? = null

    private val viewModel: ComicsOracleMainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        viewModel.characterSearchLiveData.observe(this, Observer { state ->
            state ?: return@Observer

            when (state){
                is CharacterSearchState.LoadingState -> {
                    renderLoadingState()
                }

                is CharacterSearchState.DataState -> {
                    renderDataState(state)
                }

                is CharacterSearchState.ErrorState -> {
                    renderErrorState(state)
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

        characterAdapter = ComicCharacterAdapter(requireContext())
        recyclerViewResults.setHasFixedSize(true)
        recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewResults.visibility = View.GONE

        savedSearchTerm = viewModel.getSavedSearchTerm()

        characterAdapter?.setOnItemClickListener(object: ComicCharacterAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //val intent = Intent(activity, CharacterDescriptionActivity::class.java)
                val intent = Intent(context, CharacterDescriptionWebViewActivity::class.java)
                val bundle = Bundle()
                val descr = characterAdapter?.searchedCharacters?.get(position)?.description

                descr?.apply {
                    val byte = this.toByteArray(StandardCharsets.UTF_8)
                    bundle.putByteArray(Constants.CHARACTER, byte)
                    intent.putExtras(bundle)

                    startActivity(intent)
                } ?: run {
                    startActivity(intent)
                }
            }
        })

        recyclerViewResults.adapter = characterAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchText: String): Boolean {

                characterAdapter?.clear()

                viewModel.getCharacters(searchText.trim { it <= ' ' })

                searchView.clearFocus()

                return false
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                return false
            }
        })
    }

    private fun renderDataState(dataState: CharacterSearchState.DataState) {
        if (dataState.data.isEmpty()){
            activity?.runOnUiThread {
                Toast.makeText(activity, "Could not get results", Toast.LENGTH_LONG).show()

                recyclerViewResults.visibility = View.VISIBLE
            }
        } else {
            (recyclerViewResults.adapter as ComicCharacterAdapter).populateAdapter(dataState.data)

            viewModel.clearSearchResultsPreferences()

            try {
                val savedSearchTerm = characterSearchView?.query.toString()
                val savedData = viewModel.getSavedData()

                viewModel.setSavedSearchResults(savedSearchTerm, savedData?.let { it } ?: run { "" })

            } catch (e: Exception) {
                e.printStackTrace()
            }

            progressIndicator.visibility = View.GONE
            recyclerViewResults.visibility = View.VISIBLE
        }
    }

    private fun renderLoadingState() {
        progressIndicator.visibility = View.VISIBLE
    }

    private fun renderErrorState(errorState: CharacterSearchState.ErrorState) {
        activity?.runOnUiThread {
            Toast.makeText(activity, "Could not get results", Toast.LENGTH_LONG).show()

            progressIndicator.visibility = View.GONE
            recyclerViewResults.visibility = View.VISIBLE
        }
    }
}
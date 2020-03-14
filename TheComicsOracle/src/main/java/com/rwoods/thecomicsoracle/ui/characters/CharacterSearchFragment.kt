package com.rwoods.thecomicsoracle.ui.characters

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.ui.ComicsOracleMainViewModel
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
                val description = characterAdapter?.searchedCharacters?.get(position)?.description
                var bundle: Bundle

                description?.run {
                    val byte = this.toByteArray(StandardCharsets.UTF_8)
                    bundle = bundleOf(Constants.CHARACTER to byte)
                    bundle.putByteArray(Constants.CHARACTER, byte)

                    view.findNavController().navigate(R.id.characterDescriptionWebViewFragment, bundle)

                } ?: run {
                    Toast.makeText(context, getString(R.string.character_search_no_description_available), Toast.LENGTH_LONG)
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

                viewModel.setSavedSearchResults(savedSearchTerm, savedData?.run { this } ?: run { "" })

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
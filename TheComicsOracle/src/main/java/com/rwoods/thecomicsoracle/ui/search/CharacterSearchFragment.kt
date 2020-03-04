package com.rwoods.thecomicsoracle.ui.search

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
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.ui.ComicsOracleMainActivity
import com.rwoods.thecomicsoracle.ui.description.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.util.Constants
import kotlinx.android.synthetic.main.fragment_search_results.*
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterSearchFragment : Fragment() {

    private var characterJsonAdapter: ComicCharacterJsonAdapter? = null

    private var characterSearchView: SearchView? = null

    private var savedSearchTerm: String? = null

    private lateinit var viewModel: CharacterSearchFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val fragmentName = arguments?.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(CharacterSearchFragmentViewModel::class.java)

        viewModel.progressBarLiveData.observe(this, Observer<Boolean> {
            progressIndicator.visibility = if (it) { View.VISIBLE } else { View.GONE }
        })

        viewModel.comicCharactersMutableLiveData.observe(this, Observer<MutableList<ComicCharacter>> { comicCharacters ->
            comicCharacters?.run {
                (recyclerViewResults.adapter as ComicCharacterJsonAdapter).populateAdapter(comicCharacters)

                viewModel.clearSearchResultsPreferences()

                try {
                    val savedSearchTerm = characterSearchView?.query.toString()
                    val savedData = viewModel.getSavedData()

                    viewModel.setSavedSearchResults(savedSearchTerm, savedData?.let { it } ?: run { "" })

                } catch (e: Exception) {
                    e.printStackTrace()
                }

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

        val searchView = SearchView((context as ComicsOracleMainActivity).supportActionBar?.themedContext ?: context)

        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchText: String): Boolean {

                characterJsonAdapter?.clear()

                viewModel.getCharacters(searchText.trim { it <= ' ' })

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

        characterJsonAdapter = ComicCharacterJsonAdapter(requireContext())
        recyclerViewResults.setHasFixedSize(true)
        recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewResults.visibility = View.GONE

        savedSearchTerm = viewModel.getSavedSearchTerm()

        characterJsonAdapter?.setOnItemClickListener(object: ComicCharacterJsonAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //val intent = Intent(activity, CharacterDescriptionActivity::class.java)
                val intent = Intent(context, CharacterDescriptionWebViewActivity::class.java)
                val bundle = Bundle()
                val descr = characterJsonAdapter?.searchedCharacters?.get(position)?.description

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

        recyclerViewResults.adapter = characterJsonAdapter
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
}
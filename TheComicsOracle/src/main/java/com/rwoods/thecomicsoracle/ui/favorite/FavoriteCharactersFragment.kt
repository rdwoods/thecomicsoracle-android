package com.rwoods.thecomicsoracle.ui.favorite/*
package com.rwoods.thecomicsoracle.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.ui.description.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.ui.search.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.util.Constants


/**
 * Created by rahmanwoods on 6/22/16.
 */

class FavoriteCharactersFragment : Fragment() {
    private var favComicCharacterAdapter: ComicCharacterAdapter? = null

    private var favCharacterRecyclerView: RecyclerView? = null
    private var fragmentName: String? = null

    private var viewModel: FavoriteCharacterFragmentViewModel? = null

    fun getFragmentName(): String {
        return FRAGMENT_NAME
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            fragmentName = arguments!!.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_fav_character, container, false)

        viewModel = ViewModelProviders.of(this).get(FavoriteCharacterFragmentViewModel::class.java)

        //Your RecyclerView
        favComicCharacterAdapter = ComicCharacterAdapter(activity!!.applicationContext)
        favCharacterRecyclerView = rootView.findViewById(R.id.fav_character_recycler_view) as RecyclerView
        favCharacterRecyclerView!!.setHasFixedSize(true)
        favCharacterRecyclerView!!.layoutManager = LinearLayoutManager(activity)

        viewModel!!.setUp()

        val favoriteCharacters = viewModel?.comicsOracleRepo?.getFavoritesFromDatabase()

        displayFavorites(favoriteCharacters)

        return rootView
    }

    private fun displayFavorites(favoriteCharacters: ArrayList<ComicCharacter>) {
        favComicCharacterAdapter!!.clear()

        if (!favoriteCharacters.isEmpty()) {

            favComicCharacterAdapter?.populateAdapter(favoriteCharacters)

            favCharacterRecyclerView!!.adapter = favComicCharacterAdapter

            setAdapterOnClick()
        }
    }

    fun setAdapterOnClick(){
        favComicCharacterAdapter!!.setOnItemClickListener(object: ComicCharacterAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, CharacterDescriptionWebViewActivity::class.java)
                val bundle = Bundle()
                var descr: String? = favComicCharacterAdapter!!.characters[position].description

                if (descr == null){
                    descr = ""
                }

                bundle.putString(Constants.CHARACTER, descr)
                intent.putExtras(bundle)

                startActivity(intent)
            }
        })
    }


    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        retainInstance = true
    }


    companion object {

        private val FRAGMENT_NAME = "fav_character"

        fun newInstance(): FavoriteCharactersFragment {
            val fragment = FavoriteCharactersFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
*/

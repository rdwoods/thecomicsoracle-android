package com.rwoods.thecomicsoracle.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.model.ComicCharacter
import io.realm.RealmResults

/**
 * Created by rahmanwoods on 6/22/16.
 */
class FavoriteCharactersFragment : Fragment() {
    private var mFavComicCharacterAdapter: ComicCharacterAdapter? = null

    private var mFavCharacterRecyclerView: RecyclerView? = null
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_fav_character, container, false)

        viewModel = ViewModelProviders.of(this).get(FavoriteCharacterFragmentViewModel::class.java)

        //Your RecyclerView
        mFavCharacterRecyclerView = rootView.findViewById(R.id.fav_character_recycler_view) as RecyclerView
        mFavCharacterRecyclerView!!.setHasFixedSize(true)
        mFavCharacterRecyclerView!!.layoutManager = LinearLayoutManager(activity)

        viewModel!!.setUp()

        val favoriteCharacters = viewModel?.comicsOracleRepo?.getFavoritesFromDb()

        displayFavorites(favoriteCharacters)

        return rootView
    }

    private fun displayFavorites(favoriteCharacters: RealmResults<ComicCharacter>?) {
        clearPreviousList()

        if (!favoriteCharacters?.isEmpty()!!) {

            viewModel?.favoriteCharacters!!.addAll(favoriteCharacters)

            viewModel?.mFavComicCharacterAdapter?.setComicCharacters(viewModel?.favoriteCharacters!!)

            mFavCharacterRecyclerView!!.adapter = mFavComicCharacterAdapter

            viewModel?.setAdapterOnClick()
        }
    }



    private fun clearPreviousList() {
        viewModel?.favoriteCharacters!!.clear()
    }


    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        retainInstance = true
    }

    override fun onDetach() {
        super.onDetach()
    }


    override fun onResume() {
        super.onResume()
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

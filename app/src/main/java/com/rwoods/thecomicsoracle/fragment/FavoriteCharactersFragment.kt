package com.rwoods.thecomicsoracle.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionActivity
import com.rwoods.thecomicsoracle.activity.ComicsOracleMainActivity
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

import java.util.ArrayList

import io.realm.RealmResults

/**
 * Created by rahmanwoods on 6/22/16.
 */
class FavoriteCharactersFragment : Fragment() {
    private var mFavComicCharacterAdapter: ComicCharacterAdapter? = null

    private var mFavCharacterRecyclerView: RecyclerView? = null
    private var mFavCharacterList: ArrayList<ComicCharacter>? = null
    private var fragmentName: String? = null

    fun getFragmentName(): String {
        return FRAGMENT_NAME
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            fragmentName = arguments.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
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
        val rootView = inflater!!.inflate(R.layout.fragment_fav_character, container, false)

        //Your RecyclerView
        mFavCharacterRecyclerView = rootView.findViewById(R.id.fav_character_recycler_view) as RecyclerView
        mFavCharacterRecyclerView!!.setHasFixedSize(true)
        mFavCharacterRecyclerView!!.layoutManager = LinearLayoutManager(activity)

        mFavCharacterList = ArrayList<ComicCharacter>()


        getFavoritesFromDb()

        return rootView
    }

    private fun getFavoritesFromDb() {
        val fcResults = (activity as ComicsOracleMainActivity).realm!!.where(ComicCharacter::class.java).findAll()

        mFavCharacterList!!.clear()

        if (!fcResults.isEmpty()) {

            mFavCharacterList!!.addAll(fcResults)

            mFavComicCharacterAdapter = ComicCharacterAdapter(activity, mFavCharacterList as ArrayList<ComicCharacter>)

            mFavCharacterRecyclerView!!.adapter = mFavComicCharacterAdapter

            setOnClickListener()
        }
    }


    private fun clearPreviousList() {
        mFavCharacterList!!.clear()
    }


    private fun setOnClickListener() {

        mFavComicCharacterAdapter!!.setOnItemClickListener(object: ComicCharacterAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, CharacterDescriptionActivity::class.java)

                val moshi = Moshi.Builder().build()
                val comicCharacterJsonAdapter = moshi.adapter(ComicCharacter::class.java)
                val json = comicCharacterJsonAdapter.toJson(mFavCharacterList!![position])

                val bundle = Bundle()
                bundle.putString(Constants.CHARACTER, json)
                intent.putExtras(bundle) //Put your id to your next Intent
                startActivity(intent)
            }
        })
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

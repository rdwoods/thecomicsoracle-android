package com.rwoods.thecomicsoracle.viewmodel

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.fragment.FavoriteCharactersFragment
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class FavoriteCharacterFragmentViewModel(view: FavoriteCharactersFragment?) : ComicsOracleBaseViewModel() {
    internal var mFavComicCharacterAdapter: ComicCharacterAdapter? = null

    internal var mFavCharacterList: java.util.ArrayList<ComicCharacter>? = null
    private var fragmentName: String? = null

    internal var mView: FavoriteCharactersFragment? = null

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<ComicCharacter>>? = null

    fun setUp() {
        mComicsOracleRepo = ComicsOracleRepository(mView?.activity!!)
        mFavCharacterList = ArrayList<ComicCharacter>()

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = moshi!!.adapter<List<ComicCharacter>>(type)
    }

    fun setAdapterOnClick(){
        mFavComicCharacterAdapter!!.setOnItemClickListener(object: ComicCharacterAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(mView?.activity, CharacterDescriptionWebViewActivity::class.java)
                val bundle = Bundle()
                var descr: String? = mFavComicCharacterAdapter!!.mCharacterList[position].description

                if (descr == null){
                    descr = ""
                }

                bundle.putString(Constants.CHARACTER, descr)
                intent.putExtras(bundle)

                mView?.startActivity(intent)
            }
        })
    }

    fun setVideoList(favoriteCharacterList: ArrayList<ComicCharacter>) {
        mFavCharacterList = favoriteCharacterList
    }

    init {
        this.mView = view
    }

    fun setAdapter(mFavCharacterList: java.util.ArrayList<ComicCharacter>?) {
        mFavComicCharacterAdapter = ComicCharacterAdapter(mView?.activity!!, this.mFavCharacterList as java.util.ArrayList<ComicCharacter>)
    }
}

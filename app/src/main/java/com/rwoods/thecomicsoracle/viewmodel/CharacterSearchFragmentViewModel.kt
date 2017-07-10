package com.rwoods.thecomicsoracle.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rwoods.thecomicsoracle.activity.CharacterDescriptionWebViewActivity
import com.rwoods.thecomicsoracle.adapter.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient
import com.rwoods.thecomicsoracle.fragment.CharacterSearchFragment
import com.rwoods.thecomicsoracle.model.ComicCharacter
import com.rwoods.thecomicsoracle.model.ComicCharacterResponse
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets

class CharacterSearchFragmentViewModel(view: CharacterSearchFragment?) : ComicsOracleBaseViewModel() {

    internal var mComicCharacterAdapter: ComicCharacterAdapter? = null

    internal var mComicCharacterList: ArrayList<ComicCharacter>? = null
    internal var mSearchedComicCharacterList: ArrayList<ComicCharacter>? = null

    internal var mView: CharacterSearchFragment? = null

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<ComicCharacter>>? = null

    fun setUp() {
        mComicsOracleRepo = ComicsOracleRepository(mView?.activity!!)

        mComicCharacterList = ArrayList<ComicCharacter>()
        mSearchedComicCharacterList = ArrayList<ComicCharacter>()


        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        jsonAdapter = moshi!!.adapter<List<ComicCharacter>>(type)
    }

    fun setAdapterOnClick(){
        mComicCharacterAdapter!!.setOnItemClickListener(object: ComicCharacterAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //val intent = Intent(activity, CharacterDescriptionActivity::class.java)
                val intent = Intent(mView?.context, CharacterDescriptionWebViewActivity::class.java)
                val bundle = Bundle()
                var descr: String? = mComicCharacterAdapter!!.characterList[position].description

                if (descr == null){
                    descr = ""
                }

                val byte: ByteArray? = descr.toByteArray(StandardCharsets.UTF_8)
                bundle.putByteArray(Constants.CHARACTER, byte!!)
                intent.putExtras(bundle)

                mView?.startActivity(intent)
            }
        })
    }

    fun setCharacterList(characterList: ArrayList<ComicCharacter>) {
        mComicCharacterList = characterList
    }


    fun getCharactersFromRest(searchText: String) {

        val filteredCharacterName = "name:" + searchText
        val characterSearchCall = ComicsOracleRetrofitApiRestClient.apiClient?.getCharacterByName(filteredCharacterName)

        characterSearchCall?.enqueue(object : Callback<ComicCharacterResponse> {

            override fun onResponse(call: Call<ComicCharacterResponse>, response: Response<ComicCharacterResponse>) {

                val comicCharacters = response.body()?.comicCharacters as? ArrayList<ComicCharacter>

                if (comicCharacters == null) {
                    mView?.displayCharacterSearchNotRetrieved()
                } else {
                    mView?.displayCharactersFromRest(comicCharacters)
                }
            }

            override fun onFailure(call: Call<ComicCharacterResponse>, throwable: Throwable) {
                mView?.displayCharacterSearchFailed()
            }
        })
    }

    init {
        this.mView = view
    }

    fun setCharacterAdapter(context: Context, mSearchedComicCharacterList: ArrayList<ComicCharacter>) {
        mComicCharacterAdapter = ComicCharacterAdapter(context, mSearchedComicCharacterList)
    }

    fun getCharacterAdapter(): ComicCharacterAdapter? {
        return mComicCharacterAdapter
    }

    fun getSavedData(): String? {
        return jsonAdapter!!.toJson(mComicCharacterAdapter?.mCharacterList)
    }
}

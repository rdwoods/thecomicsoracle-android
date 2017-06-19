package com.rwoods.thecomicsoracle.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.facebook.drawee.view.SimpleDraweeView
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.model.ComicCharacter

import java.util.ArrayList

/**
 * Created by rahmanwoods on 5/12/15.
 */
class ComicCharacterAdapter(private val mContext: Context, val characterList: ArrayList<ComicCharacter>) : RecyclerView.Adapter<ComicCharacterAdapter.CharacterViewHolder>() {
    val searchedCharacterList: ArrayList<ComicCharacter> = ArrayList<ComicCharacter>()
    private var mItemClickListener: OnItemClickListener? = null


    init {
        this.searchedCharacterList.addAll(characterList)
    }

    fun add(e: ComicCharacter, position: Int) {
        var position = position
        position = if (position == -1) itemCount else position
        searchedCharacterList.add(position, e)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        if (position < itemCount) {
            searchedCharacterList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var ivCharacterThumbnail: SimpleDraweeView? = null
        val tvCharacterName: TextView

        init {
            ivCharacterThumbnail = view.findViewById(R.id.character_thumbnail) as SimpleDraweeView
            tvCharacterName = view.findViewById(R.id.character_name) as TextView
            tvCharacterName.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(v, tvCharacterName.tag as Int)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.character_listrow, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val image = searchedCharacterList[position].image
        val characterName = searchedCharacterList[position].name
        val gender = searchedCharacterList[position].gender
        val thumbUrl: String? = null

        val placeholderImageId: Int
        val errorImageId: Int

        if (characterName != null) {
            holder.tvCharacterName.tag = position
            holder.tvCharacterName.text = characterName
        }

        if (gender!!.contentEquals("1")) {
            placeholderImageId = R.drawable.tco_placeholder_male
            errorImageId = R.drawable.tco_placeholder_male
        } else {
            placeholderImageId = R.drawable.tco_placeholder_female
            errorImageId = R.drawable.tco_placeholder_female
        }

        if (image != null) {
            val url = image.iconUrl

            if (url != null) {
                holder.ivCharacterThumbnail?.setImageURI(url)
            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        mItemClickListener = itemClickListener
    }


    override fun getItemCount(): Int {
        return searchedCharacterList.size
    }
}
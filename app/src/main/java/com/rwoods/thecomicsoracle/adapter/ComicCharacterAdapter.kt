package com.rwoods.thecomicsoracle.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.model.ComicCharacter
import io.realm.RealmResults

/**
 * Created by rahmanwoods on 5/12/15.
 */
class ComicCharacterAdapter(private val context: Context) : RecyclerView.Adapter<ComicCharacterAdapter.CharacterViewHolder>() {
    internal var characters: ArrayList<ComicCharacter>
    internal val searchedCharacters: ArrayList<ComicCharacter>
    private var clickListener: OnItemClickListener? = null


    init {
        this.characters = ArrayList()
        this.searchedCharacters = ArrayList()
    }

    fun add(e: ComicCharacter) {
        searchedCharacters.add(e)
        notifyItemInserted(searchedCharacters.size - 1)
    }

    fun populateAdapter(cc: ArrayList<ComicCharacter>){
        for (comicCharacter in cc){
            add(comicCharacter)
        }
    }

    fun populateAdapter(cc: RealmResults<ComicCharacter>){
        for (comicCharacter in cc){
            add(comicCharacter)
        }
    }

    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var ivCharacterThumbnail: ImageView? = null
        val tvCharacterName: TextView

        init {
            ivCharacterThumbnail = view.findViewById(R.id.character_thumbnail) as ImageView
            tvCharacterName = view.findViewById(R.id.character_name) as TextView
            tvCharacterName.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (clickListener != null) {
                clickListener!!.onItemClick(v, tvCharacterName.tag as Int)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_listrow, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val image = searchedCharacters[position].image
        val characterName = searchedCharacters[position].name
        val gender = searchedCharacters[position].gender
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
                Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.default_profile_avatar)
                    .error(R.drawable.default_profile_avatar)
                    .centerCrop()
                    .into(holder.ivCharacterThumbnail)
            }
        }
    }

    fun setComicCharacters(comicCharacters: ArrayList<ComicCharacter>){
        characters = comicCharacters
        this.searchedCharacters.addAll(characters)
        this.notifyDataSetChanged()
    }



    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        clickListener = itemClickListener
    }


    override fun getItemCount(): Int {
        return searchedCharacters.size
    }

    fun clear() {
        characters.clear()
        searchedCharacters.clear()
        this.notifyDataSetChanged()
    }
}
package com.rwoods.thecomicsoracle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.model.ComicCharacter

/**
 * Created by rahmanwoods on 5/12/15.
 */
class ComicCharacterAdapter(private val context: Context) : RecyclerView.Adapter<ComicCharacterAdapter.CharacterViewHolder>() {
    internal var characters: ArrayList<ComicCharacter> = ArrayList()
    internal val searchedCharacters: ArrayList<ComicCharacter> = ArrayList()
    private var clickListener: OnItemClickListener? = null


    fun add(e: ComicCharacter) {
        searchedCharacters.add(e)
        notifyItemInserted(searchedCharacters.size - 1)
    }

    fun populateAdapter(characterSearchResults: ArrayList<ComicCharacter>){
        characters.clear()

        for (comicCharacter in characterSearchResults){
            add(comicCharacter)
        }
    }


    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var ivCharacterThumbnail: ImageView = view.findViewById(R.id.imageViewThumbnail) as ImageView
        val tvCharacterName: TextView = view.findViewById(R.id.textViewName) as TextView

        init {
            tvCharacterName.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener?.onItemClick(v, tvCharacterName.tag as Int)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listrow, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val image = searchedCharacters[position].image
        val characterName = searchedCharacters[position].name
        val gender = searchedCharacters[position].gender
        val thumbUrl: String? = null

        characterName?.apply {
            holder.tvCharacterName.tag = position
            holder.tvCharacterName.text = this
        }

        val placeholderImageId: Int = gender?.let { value ->
            if (value.contentEquals("1")){
                R.drawable.tco_placeholder_male
            } else {
                R.drawable.tco_placeholder_female
            }
        } ?: run {
            R.drawable.tco_placeholder_male
        }


        image?.apply {
            val url = this.iconUrl

            url?.apply {
                Glide.with(context)
                        .load(this)
                        .placeholder(placeholderImageId)
                        .error(R.drawable.default_profile_avatar)
                        .centerCrop()
                        .into(holder.ivCharacterThumbnail)
            }
        }
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
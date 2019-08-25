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
import com.rwoods.thecomicsoracle.model.Video

/**
 * Created by rahmanwoods on 5/12/15.
 */
class VideoAdapter(private val context: Context) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    internal var videos: ArrayList<Video> = ArrayList()
    var searchedVideos: ArrayList<Video> = ArrayList()
    private var clickListener: OnItemClickListener? = null


    fun add(e: Video) {
        searchedVideos.add(e)
        notifyItemInserted(searchedVideos.size - 1)
    }

    fun populateAdapter(videoSearchResults: ArrayList<Video>){
        videos.clear()

        for (video in videoSearchResults){
            add(video)
        }
    }

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var ivVideoThumbnail: ImageView = view.findViewById(R.id.imageViewThumbnail) as ImageView
        val tvVideoTitle: TextView = view.findViewById(R.id.textViewName) as TextView

        init {
            tvVideoTitle.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener?.onItemClick(v, tvVideoTitle.tag as Int)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listrow, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val image = searchedVideos[position].image
        val videoTitle = searchedVideos[position].name

        videoTitle?.apply {
            holder.tvVideoTitle.tag = position
            holder.tvVideoTitle.text = this
        }

        image?.apply {
            val url = this.iconUrl

            url?.apply {
                Glide.with(context)
                        .load(this)
                        .placeholder(R.drawable.default_profile_avatar)
                        .error(R.drawable.default_profile_avatar)
                        .centerCrop()
                        .into(holder.ivVideoThumbnail)
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
        return searchedVideos.size
    }

    fun clear() {
        videos.clear()
        searchedVideos.clear()
        this.notifyDataSetChanged()
    }
}
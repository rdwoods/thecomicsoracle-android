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
import com.rwoods.thecomicsoracle.model.Video
import java.util.*

/**
 * Created by rahmanwoods on 5/12/15.
 */
class VideoAdapter(private val mContext: Context, val videoList: ArrayList<Video>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    val searchedVideoList: ArrayList<Video> = ArrayList()
    private var mItemClickListener: OnItemClickListener? = null


    init {
        this.searchedVideoList.addAll(this.videoList)
    }

    fun add(e: Video, position: Int) {
        var position = position
        position = if (position == -1) itemCount else position
        searchedVideoList.add(position, e)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        if (position < itemCount) {
            searchedVideoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var ivVideoThumbnail: ImageView? = null
        val tvVideoTitle: TextView

        init {
            ivVideoThumbnail = view.findViewById(R.id.video_thumbnail) as ImageView
            tvVideoTitle = view.findViewById(R.id.video_name) as TextView
            tvVideoTitle.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(v, tvVideoTitle.tag as Int)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.video_listrow, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val image = searchedVideoList[position].image
        val characterName = searchedVideoList[position].name

        if (characterName != null) {
            holder.tvVideoTitle.tag = position
            holder.tvVideoTitle.text = characterName
        }

        if (image != null) {
            val url = image.iconUrl

            if (url != null) {
                Glide.with(mContext)
                        .load(url)
                        .placeholder(R.drawable.default_profile_avatar)
                        .error(R.drawable.default_profile_avatar)
                        .centerCrop()
                        .into(holder.ivVideoThumbnail)
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
        return searchedVideoList.size
    }
}
package com.rwoods.thecomicsoracle.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rwoods.thecomicsoracle.activity.VideoViewActivity
import com.rwoods.thecomicsoracle.adapter.VideoAdapter
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient
import com.rwoods.thecomicsoracle.fragment.VideoSearchFragment
import com.rwoods.thecomicsoracle.model.Video
import com.rwoods.thecomicsoracle.model.VideoResponse
import com.rwoods.thecomicsoracle.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoSearchFragmentViewModel(view: VideoSearchFragment?) : ComicsOracleBaseViewModel(), Callback<VideoResponse> {


    internal var mVideoAdapter: VideoAdapter? = null

    internal var mVideoList: ArrayList<Video>? = null
    internal var mSearchedVideoList: ArrayList<Video>? = null

    internal var mView: VideoSearchFragment? = null

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<Video>>? = null

    fun setUp() {
        mVideoList = ArrayList<Video>()
        mSearchedVideoList = ArrayList<Video>()

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Video::class.java)
        jsonAdapter = moshi!!.adapter<List<Video>>(type)
    }

    fun setAdapterOnClick(){
        mVideoAdapter!!.setOnItemClickListener(object: VideoAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //val intent = Intent(activity, CharacterDescriptionActivity::class.java)
                val intent = Intent(mView?.context, VideoViewActivity::class.java)
                val bundle = Bundle()
                var url: String? = mVideoAdapter!!.mVideoList[position].highUrl

                if (url == null){
                    url = ""
                }

                bundle.putString(Constants.VIDEO_URL, url)
                intent.putExtras(bundle)

                mView?.startActivity(intent)
            }
        })
    }

    fun setVideoList(videoList: ArrayList<Video>) {
        mVideoList = videoList
    }


    fun getVideosFromRest(searchText: String) {

        val filteredVideoName = "name:" + searchText
        val videoSearchCall = ComicsOracleRetrofitApiRestClient.apiClient?.getVideoByName(filteredVideoName)

        videoSearchCall?.enqueue(this)
    }

    init {
        this.mView = view
    }

    fun setVideoAdapter(context: Context, mSearchedVideoList: ArrayList<Video>) {
        mVideoAdapter = VideoAdapter(context, mSearchedVideoList)
    }


    fun getSavedData(): String? {
        return jsonAdapter!!.toJson(mVideoAdapter?.mVideoList)
    }

    override fun onResponse(call: Call<VideoResponse>?, response: Response<VideoResponse>?) {

        val videos = response?.body()?.videos as? ArrayList<Video>

        if (videos == null) {
            mView?.displayVideoSearchNotRetrieved()
        } else {
            mView?.displayVideosFromRest(videos)
        }
    }

    override fun onFailure(call: Call<VideoResponse>, throwable: Throwable) {
        mView?.displayVideoSearchFailed()
    }
}

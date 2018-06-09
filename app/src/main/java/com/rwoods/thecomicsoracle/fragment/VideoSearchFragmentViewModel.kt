package com.rwoods.thecomicsoracle.fragment

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient
import com.rwoods.thecomicsoracle.model.Video
import com.rwoods.thecomicsoracle.model.VideoResponse
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoSearchFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application), Callback<VideoResponse> {

    internal var videosList: ArrayList<Video>? = null
    internal var searchedVideoList: ArrayList<Video>? = null

    internal var mView: VideoSearchFragment? = null

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<Video>>? = null

    internal var videos: MutableLiveData<ArrayList<Video>>? = null

    fun setUp() {
        comicsOracleRepo = ComicsOracleRepository(application)
        videosList = ArrayList()
        searchedVideoList = ArrayList()

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Video::class.java)
        jsonAdapter = moshi!!.adapter<List<Video>>(type)
    }


    fun setVideoList(videoList: ArrayList<Video>) {
        videosList = videoList
    }


    fun getVideosFromRest(searchText: String) {

        val filteredVideoName = "name:" + searchText
        val videoSearchCall = ComicsOracleRetrofitApiRestClient.apiClient?.getVideoByName(filteredVideoName)

        videoSearchCall?.enqueue(this)
    }


    fun getSavedData(): String? {
        return jsonAdapter!!.toJson(videosList)
    }

    override fun onResponse(call: Call<VideoResponse>?, response: Response<VideoResponse>) {

        val videoResponse = response.body()?.videos as? ArrayList<Video>

        if (videoResponse == null) {
            videos!!.postValue(null)
        } else {
            videos!!.postValue(videoResponse)
        }
    }

    override fun onFailure(call: Call<VideoResponse>, throwable: Throwable) {
        videos!!.postValue(null)
    }
}

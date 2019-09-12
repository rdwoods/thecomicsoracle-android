package com.rwoods.thecomicsoracle.ui.video

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rwoods.thecomicsoracle.data.model.Video
import com.rwoods.thecomicsoracle.data.remote.response.VideoResponse
import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.ui.base.ComicsOracleBaseViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoSearchFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application){

    internal var searchedVideoList: ArrayList<Video>? = null

    var progressBarLiveData: MutableLiveData<Boolean> = MutableLiveData()

    internal var moshi: Moshi? = null

    internal var jsonAdapter: JsonAdapter<List<Video>>? = null

    internal var videos = MutableLiveData<ArrayList<Video>>()

    init {
        comicsOracleRepo = ComicsOracleRepository(application)
        searchedVideoList = ArrayList()

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Video::class.java)
        jsonAdapter = moshi?.adapter<List<Video>>(type)
    }

    fun getVideosFromRest(searchText: String) {

        progressBarLiveData.postValue(true)

        val filteredVideoName = "name:$searchText"

        comicsOracleRepo.retrofitWrapper.createComicsOracleService().getVideoByName(filteredVideoName).enqueue(object : Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                val videoResponse = response.body()?.videos as? ArrayList<Video>

                progressBarLiveData.postValue(false)

                videoResponse?.apply {
                    videos.postValue(videoResponse)
                } ?: run {
                    videos.postValue(null)
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                progressBarLiveData.postValue(false)

                videos.postValue(null)
            }
        })
    }
}

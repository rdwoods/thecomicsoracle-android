package com.rwoods.thecomicsoracle.ui.video

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rwoods.thecomicsoracle.data.model.Video
import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.ui.base.ComicsOracleBaseViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoSearchFragmentViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application){

    internal var searchedVideoList = mutableListOf<Video>()

    var progressBarLiveData: MutableLiveData<Boolean> = MutableLiveData()

    internal var moshi: Moshi? = null

    internal var jsonAdapter: VideoAdapter

    internal var videosMutableLiveData = MutableLiveData<MutableList<Video>>()

    init {
        comicsOracleRepo = ComicsOracleRepository(application)
        searchedVideoList = ArrayList()

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Video::class.java)
        jsonAdapter = VideoAdapter(application)
    }

    fun getVideos(searchText: String) {

        progressBarLiveData.postValue(true)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Dispatchers.IO
                /* perform blocking network IO here */
                val videos = comicsOracleRepo.getVideosFromRest(searchText)
                progressBarLiveData.postValue(false)

                videos?.run {
                    videosMutableLiveData.postValue(this)
                } ?: run {
                    progressBarLiveData.postValue(false)
                    videosMutableLiveData.postValue(null)
                }
            }
        }
    }
}

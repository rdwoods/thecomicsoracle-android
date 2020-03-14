package com.rwoods.thecomicsoracle.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LevelListDrawable
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rwoods.thecomicsoracle.R
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.model.Video
import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import com.rwoods.thecomicsoracle.ui.base.ComicsOracleBaseViewModel
import com.rwoods.thecomicsoracle.ui.characters.CharacterSearchState
import com.rwoods.thecomicsoracle.ui.characters.ComicCharacterAdapter
import com.rwoods.thecomicsoracle.ui.description.TextViewImageStore
import com.rwoods.thecomicsoracle.ui.videos.VideoAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.*

class ComicsOracleMainViewModel(internal var application: Application) : ComicsOracleBaseViewModel(application) {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since we pass viewModelJob, you can cancel all coroutines
     * launched by uiScope by calling viewModelJob.cancel()
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val bitmapScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    internal var moshi: Moshi

    internal var characterAdapter: ComicCharacterAdapter
    internal var videoAdapter: VideoAdapter

    internal var savedCharacters = mutableListOf<ComicCharacter>()

    internal var videosMutableLiveData = MutableLiveData<MutableList<Video>>()

    internal var characterSearchLiveData = MutableLiveData<CharacterSearchState>()

    var descriptionLiveData = MutableLiveData<TextViewImageStore>()

    internal lateinit var characterSearchState: CharacterSearchState

    init {
        comicsOracleRepo = ComicsOracleRepository(application)

        moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ComicCharacter::class.java)
        characterAdapter = ComicCharacterAdapter(application)
        videoAdapter = VideoAdapter(application)
    }


    fun getCharacters(searchText: String) {
        characterSearchLiveData.postValue(CharacterSearchState.LoadingState)

        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Dispatchers.IO
                /* perform blocking network IO here */
                val characters = comicsOracleRepo.getCharactersFromRest(searchText)

                characters?.run {
                    savedCharacters = this
                    characterSearchState = CharacterSearchState.DataState(this)
                } ?: run {
                    characterSearchState = CharacterSearchState.ErrorState("Error")
                }

                characterSearchLiveData.postValue(characterSearchState)
            }
        }
    }

    fun getVideos(searchText: String) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Dispatchers.IO
                /* perform blocking network IO here */
                val videos = comicsOracleRepo.getVideosFromRest(searchText)

                videos?.run {
                    videosMutableLiveData.postValue(this)
                } ?: run {
                    videosMutableLiveData.postValue(null)
                }
            }
        }
    }

    suspend fun processHtml(textViewImageStore: TextViewImageStore, source: String, windowSize: Point, levelListDrawable: LevelListDrawable) {
        var textView: TextView?
        var width: Int = 0
        var height: Int = 0
        lateinit var bitmap: Bitmap

        val bitmapJob = bitmapScope.async(Dispatchers.Default) {
            textView = textViewImageStore.textView
            width = textViewImageStore.intrinsicWidth
            height = textViewImageStore.intrinsicHeight

            try {
                bitmap= Glide
                        .with(application)
                        .asBitmap()
                        .load(source)
                        .apply(RequestOptions()
                                .centerCrop()
                                .dontTransform()
                                .error(R.drawable.default_profile_avatar))
                        .submit(480, 480) // Width and height
                        .get()


            } catch (e: Exception) {
            }

            try {
                val d = BitmapDrawable(application.resources, bitmap)

                // Lets calculate the ratio according to the screen width in px
                val multiplier = windowSize.x / bitmap.width
                //Log.d(LOG_CAT, "multiplier: " + multiplier);
                levelListDrawable.addLevel(1, 1, d)
                // Set bounds width  and height according to the bitmap resized size
                levelListDrawable.setBounds(0, 0, bitmap.width * multiplier, bitmap.height * multiplier)
                levelListDrawable.level = 1
                textView?.text = textView?.text // invalidate() doesn't work correctly...


            } catch (e: Exception) {

            }
        }

        bitmapJob.join()

        descriptionLiveData.postValue(textViewImageStore)

    }


    fun getSavedData(): String? {
        return characterAdapter.toString()
    }


    fun setSavedSearchResults(savedSearchTerm: String, savedData: String) {
        comicsOracleRepo.sharedPreferencesHelper.setSavedSearchResults(savedSearchTerm, savedData)
    }

    fun clearSearchResultsPreferences() {
        comicsOracleRepo.sharedPreferencesHelper.clearSearchResultsPreferences()
    }

    fun getSavedSearchTerm(): String? {
        return comicsOracleRepo.sharedPreferencesHelper.savedSearchTerm
    }


}

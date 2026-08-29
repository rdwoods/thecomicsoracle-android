package com.rwoods.thecomicsoracle.data.repository

import android.content.Context
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.model.ComicVideo
import com.rwoods.thecomicsoracle.data.preferences.SharedPreferencesHelper
import com.rwoods.thecomicsoracle.data.service.ComicsOracleApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ComicsOracleRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val characterApi: ComicsOracleApiService
) : ComicsOracleRepository {

    override val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(context)

    override fun getCharactersFromRest(searchText: String): Flow<List<ComicCharacter>> = flow {
        val filteredCharacterName = "name:$searchText"

        val charactersResponse = safeApiCall(
            call = { characterApi.getCharacterByNameAsync(filteredCharacterName, 20) },
            errorMessage = "Error Fetching Characters"
        )

        emit(charactersResponse?.comicCharacters ?: emptyList())
    }

    override fun getVideosFromRest(searchText: String): Flow<List<ComicVideo>> = flow {
        val filteredCharacterName = "name:$searchText"

        val videoResponse = safeApiCall(
            call = { characterApi.getVideoByNameAsync(filteredCharacterName, 20) },
            errorMessage = "Error Fetching Videos"
        )

        emit(value = videoResponse?.comicVideos ?: emptyList())
    }

    override fun getFavoritesFromDatabase(): Flow<List<ComicCharacter>> {
        TODO("Not yet implemented")
    }

    override fun selectOrUnselectFavoriteCharacter(
        selected: Boolean,
        character: ComicCharacter
    ): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}

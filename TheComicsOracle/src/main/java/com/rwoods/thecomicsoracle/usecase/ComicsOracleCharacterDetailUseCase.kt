package com.rwoods.thecomicsoracle.usecase

import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import javax.inject.Inject

class ComicsOracleCharacterDetailUseCase @Inject constructor(
    val comicsOracleRepository: ComicsOracleRepository
) {
    operator fun invoke(selected: Boolean, character: ComicCharacter) =
        comicsOracleRepository.selectOrUnselectFavoriteCharacter(selected, character)
}
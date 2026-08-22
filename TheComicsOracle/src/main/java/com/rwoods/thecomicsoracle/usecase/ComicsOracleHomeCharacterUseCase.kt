package com.rwoods.thecomicsoracle.usecase

import com.rwoods.thecomicsoracle.data.repository.ComicsOracleRepository
import javax.inject.Inject

class ComicsOracleHomeCharacterUseCase @Inject constructor(
    val comicsOracleRepository: ComicsOracleRepository
) {
    operator fun invoke(searchText: String) = comicsOracleRepository.getCharactersFromRest(searchText = searchText)
}
package com.rwoods.thecomicsoracle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.ui.ComicsOracleCharacterDetailViewModel
import com.rwoods.thecomicsoracle.ui.ComicsOracleHomeViewModel
import com.rwoods.thecomicsoracle.ui.Destination
import kotlinx.serialization.json.Json

@Composable
fun ComicsOracleNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        modifier = modifier
    ) {

        composable(Destination.Home.route) {
            val viewModel: ComicsOracleHomeViewModel = hiltViewModel()

            ComicsOracleHomeScreen(viewModel = viewModel, onCharacterClick = { character ->
                navController.navigate(Destination.CharacterDetails.createRoute(character))
            })
        }

        composable(
            Destination.CharacterDetails.route,
            arguments = Destination.CharacterDetails.arguments
        ) { backStackEntry ->
            val characterJson = backStackEntry.arguments?.getString("character")
            val character = characterJson?.let { Json.decodeFromString<ComicCharacter>(it) }

            val viewModel: ComicsOracleCharacterDetailViewModel = hiltViewModel()

            ComicsOracleCharacterDetailScreen(
                viewModel = viewModel,
                character = character,
                onFavoriteSelected = { selected, character ->
                    // Handle favorite selection
                }
            )
        }
    }
}

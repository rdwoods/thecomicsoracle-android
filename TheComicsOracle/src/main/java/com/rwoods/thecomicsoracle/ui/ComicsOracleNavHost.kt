package com.rwoods.thecomicsoracle.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.data.model.ComicVideo
import com.rwoods.thecomicsoracle.viewmodel.ComicsOracleCharacterDetailViewModel
import com.rwoods.thecomicsoracle.viewmodel.ComicsOracleHomeViewModel
import com.rwoods.thecomicsoracle.viewmodel.ComicsOracleVideoViewModel
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

            ComicsOracleHomeScreen(
                viewModel = viewModel,
                onCharacterClick = { character ->
                    navController.navigate(Destination.CharacterDetails.createRoute(character))
                },
                onVideoClick = { video -> navController.navigate(Destination.Video.createRoute(video)) })
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

        composable(Destination.Video.route,
            arguments = Destination.Video.arguments){ backStackEntry ->
            val viewModel: ComicsOracleVideoViewModel = hiltViewModel()

            val videoJson = backStackEntry.arguments?.getString("video")
            val comicVideo = videoJson?.let { Json.decodeFromString<ComicVideo>(it) }

            ComicsOracleVideoScreen(
                viewModel = viewModel,
                comicVideo = comicVideo
            )
        }
    }
}

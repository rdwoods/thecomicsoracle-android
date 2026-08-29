package com.rwoods.thecomicsoracle.ui

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.net.Uri
import com.rwoods.thecomicsoracle.data.model.ComicVideo

sealed class Destination(val route: String) {
    data object Home : Destination("home")
    
    data object CharacterDetails : Destination("character_details/{character}") {
        val arguments = listOf(
            navArgument("character") { type = NavType.StringType }
        )
        
        fun createRoute(character: ComicCharacter): String {
            val json = Json.encodeToString(character)
            return "character_details/${Uri.encode(json)}"
        }
    }


    data object Video: Destination(route = "video/{video}"){
        val arguments = listOf(
            navArgument("video") { type = NavType.StringType }
        )
        fun createRoute(comicVideo: ComicVideo): String {
            val json = Json.encodeToString(comicVideo)
            return "video/${Uri.encode(json)}"
        }
    }
}
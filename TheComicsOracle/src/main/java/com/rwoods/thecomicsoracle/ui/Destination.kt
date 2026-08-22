package com.rwoods.thecomicsoracle.ui

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.net.Uri

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
}
package com.rwoods.thecomicsoracle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import com.rwoods.thecomicsoracle.data.model.ComicCharacter
import com.rwoods.thecomicsoracle.ui.ComicsOracleCharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun ComicsOracleCharacterDetailScreen(
    viewModel: ComicsOracleCharacterDetailViewModel,
    character: ComicCharacter?,
    onFavoriteSelected: (Boolean, ComicCharacter) -> Unit,
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    character?.let { 
                        Text(text = AnnotatedString.fromHtml(it.name)) 
                    } ?: Text("Character Detail")
                },
                actions = {
                    character?.let { char ->
                        IconButton(onClick = { onFavoriteSelected(!state.value.selected, char) }) {
                            Icon(
                                imageVector = if (state.value.selected) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (state.value.selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            character?.let {
                Text(
                    text = AnnotatedString.fromHtml(it.name), 
                    style = MaterialTheme.typography.headlineLarge
                )
                
                Text(
                    text = "Gender: ${if (it.gender == "1") "Male" else "Female"}", 
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                it.description?.let { desc ->
                    Text(
                        text = AnnotatedString.fromHtml(desc), 
                        style = MaterialTheme.typography.bodyMedium, 
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
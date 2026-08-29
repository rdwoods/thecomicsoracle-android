package com.rwoods.thecomicsoracle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import com.rwoods.thecomicsoracle.data.model.ComicVideo
import com.rwoods.thecomicsoracle.ui.state.VideoIntent
import com.rwoods.thecomicsoracle.viewmodel.ComicsOracleVideoViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)

@Composable
fun ComicsOracleVideoScreen(
    viewModel: ComicsOracleVideoViewModel,
    comicVideo: ComicVideo?
){
    val state = viewModel.state.collectAsState()

    var currentVideoUrl by remember {
        mutableStateOf(comicVideo?.highUrl ?: "")
    }

    // 1. Initialize ExoPlayer safely and remember it
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("The Comics Oracle") }
                )
            }
        }
    ) { padding ->
        when {
            state.value.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                if (currentVideoUrl.isNotEmpty()){
                    viewModel.onIntent(VideoIntent.LoadVideoUrl)
                }
            }

            state.value.videoLoaded -> {
                Column(modifier = Modifier.padding(padding)) {
                    state.value.highResVideoUrl?.apply {
                        currentVideoUrl = this
                    }

                    VideoPlayer(currentVideoUrl, modifier = Modifier.padding(padding))

                    Row(modifier = Modifier.fillMaxSize()) {
                        Button(onClick = {
                            state.value.highResVideoUrl?.apply {
                                currentVideoUrl = this
                            }
                        }) {
                            Text(text = "Play High Res")
                        }

                        Button(onClick = {
                            state.value.lowResVideoUrl?.apply {
                                currentVideoUrl = this
                            }
                        }) {
                            Text(text = "Play Low Res")
                        }
                    }
                }
            }
        }
    }
}
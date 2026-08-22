package com.rwoods.thecomicsoracle

import android.app.Application
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.rwoods.thecomicsoracle.theme.AppBackground
import com.rwoods.thecomicsoracle.ui.ComicsOracleNavHost
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by rwoods on 2/23/2016.
 */
@HiltAndroidApp
class ComicsOracleApplication : Application()

@Composable
fun ComicsOracleApp() {
    Surface(color = AppBackground) {
        ComicsOracleNavHost()
    }
}

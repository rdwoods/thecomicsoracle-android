package com.rwoods.thecomicsoracle.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rwoods.thecomicsoracle.ComicsOracleApp
import com.rwoods.thecomicsoracle.theme.ComicsOracleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsOracleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicsOracleTheme {
                ComicsOracleApp()
            }
        }
    }
}

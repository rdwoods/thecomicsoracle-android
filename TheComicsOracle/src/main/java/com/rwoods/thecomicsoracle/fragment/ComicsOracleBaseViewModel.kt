package com.rwoods.thecomicsoracle.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository

/**
 * Created by rahmandunbarwoods on 7/3/17.
 */

open class ComicsOracleBaseViewModel(application: Application) : AndroidViewModel(application) {
    internal lateinit var comicsOracleRepo: ComicsOracleRepository
}

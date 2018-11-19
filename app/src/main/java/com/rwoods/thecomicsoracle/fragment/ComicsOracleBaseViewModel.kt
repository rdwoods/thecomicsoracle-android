package com.rwoods.thecomicsoracle.fragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import com.rwoods.thecomicsoracle.repository.ComicsOracleRepository

/**
 * Created by rahmandunbarwoods on 7/3/17.
 */

open class ComicsOracleBaseViewModel(application: Application) : AndroidViewModel(application) {
    internal var comicsOracleRepo: ComicsOracleRepository? = null
}

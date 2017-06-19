package com.rwoods.thecomicsoracle.util

/**
 * Created by rwoods on 3/7/2016.
 */
class APIError {

    private val statusCode: Int = 0
    private val message: String? = null

    fun status(): Int {
        return statusCode
    }

    fun message(): String {
        return message as String
    }
}
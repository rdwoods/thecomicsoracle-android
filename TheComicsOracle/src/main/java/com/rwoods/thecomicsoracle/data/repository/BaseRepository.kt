package com.rwoods.thecomicsoracle.data.repository

import retrofit2.Response
import timber.log.Timber
import java.io.IOException

open class BaseRepository{

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result : Result<T> = safeApiResult(call,errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success -> {
                data = result.data
            }

            is Result.Error -> {
                Timber.d("$errorMessage & Exception - ${result.exception}")
            }
        }

        return data

    }

    private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>, errorMessage: String) : Result<T>{
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val responseData = response.body()
                responseData?.run {
                    return Result.Success(this)
                }
            }
            
            // Detailed logging for failed responses
            val errorBody = response.errorBody()?.string()
            Timber.e("API ERROR: Code ${response.code()}, Message: ${response.message()}, Body: $errorBody")
            
            return Result.Error(IOException("Error Occurred: Code ${response.code()}, Custom ERROR - $errorMessage"))
        } catch (e: Exception) {
            Timber.e(e, "API EXCEPTION: $errorMessage")
            return Result.Error(e)
        }
    }
}
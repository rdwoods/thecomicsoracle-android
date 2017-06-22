package com.rwoods.thecomicsoracle.util

import com.rwoods.thecomicsoracle.api.ComicsOracleRetrofitApiRestClient
import retrofit2.Response
import java.io.IOException

/**
 * Created by rwoods on 3/7/2016.
 */
object ErrorUtils {

    fun parseError(response: Response<*>): APIError {
        val converter = ComicsOracleRetrofitApiRestClient.retrofit!!
                .responseBodyConverter<APIError>(APIError::class.java, arrayOfNulls<Annotation>(0))

        val error: APIError

        try {
            error = converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            return APIError()
        }

        return error
    }
}
package com.rwoods.thecomicsoracle.data.model

import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ComicVideo(

        var id: Long = 0,

        var name: String? = null,

        @Json(name = "image")
        var image: Image? = null,

        @Json(name = "high_url")
        var highUrl: String? = null,

        @Json(name = "low_url")
        var lowUrl: String? = null
)
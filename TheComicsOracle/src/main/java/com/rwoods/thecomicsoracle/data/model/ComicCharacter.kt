package com.rwoods.thecomicsoracle.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ComicCharacter(
        @PrimaryKey val id: Int,

        @ColumnInfo(name = "name") var name: String = "",

        @ColumnInfo(name = "gender") var gender: String = "",

        @Json(name = "image")
        @ColumnInfo(name = "image")
        var image: Image? = null,

        var description: String? = null
)
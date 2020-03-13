package com.rwoods.thecomicsoracle.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
//@JsonClass(generateAdapter = true)
data class ComicCharacter(
        @PrimaryKey val id: Int,

        @ColumnInfo(name = "name") var name: String? = null,

        @ColumnInfo(name = "gender") var gender: String? = null,

        @Json(name = "image")
        @ColumnInfo(name = "image")
        var image: Image? = null,

        var description: String? = null
)
package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ComicCharacter : RealmObject() {

    @PrimaryKey
    var id: Long = 0

    var name: String? = null

    var gender: String? = null

    @Json(name = "image")
    var image: Image? = null

    var description: String? = null
}
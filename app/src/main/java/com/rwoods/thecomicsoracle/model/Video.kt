package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Video : RealmObject() {

    @PrimaryKey
    var id: Long = 0

    var name: String? = null

    @Json(name = "image")
    var image: Image? = null

    @Json(name = "high_url")
    var highUrl: String? = null

    @Json(name = "low_url")
    var lowUrl: String? = null
}
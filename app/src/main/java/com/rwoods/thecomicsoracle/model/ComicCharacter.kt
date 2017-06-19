package com.rwoods.thecomicsoracle.model

import com.squareup.moshi.Json
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class ComicCharacter : RealmObject() {

    @PrimaryKey
    open var id: Long = 0

    open var name: String? = null

    open var gender: String? = null

    @Json(name = "image")
    open var image: Image? = null

    open var description: String? = null
}
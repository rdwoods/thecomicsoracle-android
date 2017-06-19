package com.rwoods.thecomicsoracle.model

import io.realm.RealmObject

open class User : RealmObject {

    var username: String? = null

    var password: String? = null

    constructor() {}

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}
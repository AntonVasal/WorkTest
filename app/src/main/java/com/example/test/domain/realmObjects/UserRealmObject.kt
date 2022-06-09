package com.example.test.domain.realmObjects

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserRealmObject : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var login: String = ""
    var nodeId: String = ""
    var avatarUrl: String = ""
    var reposUrl: String = ""
}
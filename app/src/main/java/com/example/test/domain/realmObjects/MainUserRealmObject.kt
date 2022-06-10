package com.example.test.domain.realmObjects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MainUserRealmObject: RealmObject() {
    @PrimaryKey
    var id:Int = 0
    var changesCount : Int = 0
}
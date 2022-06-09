package com.example.test.domain.realmObjects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ReposRealmObject : RealmObject() {
    @PrimaryKey
    var id:Int = 0
    var repos:ArrayList<String> = ArrayList()
}
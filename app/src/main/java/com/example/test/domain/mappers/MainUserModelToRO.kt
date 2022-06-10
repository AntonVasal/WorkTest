package com.example.test.domain.mappers

import com.example.test.domain.models.MainUserModel
import com.example.test.domain.realmObjects.MainUserRealmObject

fun mainUserModelToRo(mainUserModel: MainUserModel):MainUserRealmObject{
    val obj = MainUserRealmObject()
    obj.id  = mainUserModel.id
    obj.changesCount = mainUserModel.changesCount
    return obj
}
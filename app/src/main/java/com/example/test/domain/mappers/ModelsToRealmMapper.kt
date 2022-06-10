package com.example.test.domain.mappers

import com.example.test.domain.models.MainUserModel
import com.example.test.domain.models.UserModel
import com.example.test.domain.realmObjects.MainUserRealmObject
import com.example.test.domain.realmObjects.UserRealmObject


fun mainUserModelToRo(mainUserModel: MainUserModel): MainUserRealmObject {
    val obj = MainUserRealmObject()
    obj.id  = mainUserModel.id
    obj.changesCount = mainUserModel.changesCount
    return obj
}

fun modelToRealm(userModel: UserModel): UserRealmObject {
    val userRealmObject = UserRealmObject()
    userRealmObject.id = userModel.id
    userRealmObject.login = userModel.login
    userRealmObject.nodeId = userModel.nodeId
    userRealmObject.avatarUrl = userModel.avatarUrl
    userRealmObject.reposUrl = userModel.reposUrl
    return userRealmObject
}
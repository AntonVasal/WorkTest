package com.example.test.domain.mappers

import com.example.test.domain.models.UserModel
import com.example.test.domain.realmObjects.UserRealmObject

fun modelToRealm(userModel: UserModel): UserRealmObject {
    val userRealmObject = UserRealmObject()
    userRealmObject.id = userModel.id
    userRealmObject.login = userModel.login
    userRealmObject.nodeId = userModel.nodeId
    userRealmObject.avatarUrl = userModel.avatarUrl
    userRealmObject.reposUrl = userModel.reposUrl
    return userRealmObject
}


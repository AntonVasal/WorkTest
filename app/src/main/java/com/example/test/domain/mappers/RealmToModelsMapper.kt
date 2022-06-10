package com.example.test.domain.mappers

import com.example.test.domain.models.MainUserModel
import com.example.test.domain.models.UserModel
import com.example.test.domain.realmObjects.MainUserRealmObject
import com.example.test.domain.realmObjects.UserRealmObject

fun mainUserRealmToModel(obj: MainUserRealmObject) = MainUserModel(obj.id,obj.changesCount)

fun realmToModel(userRealmObject: UserRealmObject): UserModel = UserModel(
    userRealmObject.id,
    userRealmObject.login,
    userRealmObject.nodeId,
    userRealmObject.avatarUrl,
    userRealmObject.reposUrl
)
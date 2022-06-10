package com.example.test.domain.mappers

import com.example.test.domain.models.MainUserModel
import com.example.test.domain.realmObjects.MainUserRealmObject

fun mainUserRealmToModel(obj:MainUserRealmObject) = MainUserModel(obj.id,obj.changesCount)
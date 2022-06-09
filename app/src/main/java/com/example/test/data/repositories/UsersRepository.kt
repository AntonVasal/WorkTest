package com.example.test.data.repositories

import com.example.test.data.network.API
import com.example.test.domain.mappers.modelToRealm
import com.example.test.domain.models.UserModel
import com.example.test.domain.realmObjects.UserRealmObject
import io.realm.Realm
import javax.inject.Inject

class UsersRepository @Inject constructor(private val api: API, private val realm: Realm) {

    suspend fun getUsersWithRetrofit(): ArrayList<UserModel> = api.getUsers().body()!!

    fun getUsersWithRealm(): List<UserRealmObject> = realm.where(UserRealmObject::class.java).findAll().toList()

    fun setUsersToRealm(list: ArrayList<UserModel>) {
        val arrayList = ArrayList<UserRealmObject>()
        for (model in list) {
            arrayList.add(modelToRealm(model))
        }
        realm.executeTransactionAsync {
            for (realmObj in arrayList) {
                it.insertOrUpdate(realmObj)
            }
        }
    }

    suspend fun getUsersReposWithRetrofit(login:String):ArrayList<String> = api.getRepos(login).body()!!

    fun getReposWithRealm(login: String,id:Int):ArrayList<String> = 


}
package com.example.test.data.repositories

import com.example.test.data.network.API
import com.example.test.domain.mappers.mainUserModelToRo
import com.example.test.domain.mappers.modelToRealm
import com.example.test.domain.models.MainUserModel
import com.example.test.domain.models.ReposModel
import com.example.test.domain.models.UserModel
import com.example.test.domain.realmObjects.MainUserRealmObject
import com.example.test.domain.realmObjects.ReposRealmObject
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

    suspend fun getUsersReposWithRetrofit(login:String):ArrayList<ReposModel> = api.getRepos(login).body()!!

    fun getReposWithRealm(id:Int): List<String>? = realm.where(ReposRealmObject::class.java)
        .equalTo("id",id).findFirst()?.repos?.toList()

    fun setReposToRealm(list: ArrayList<String>,id: Int){
        realm.executeTransactionAsync {
            val obj = ReposRealmObject()
            obj.id = id
            obj.repos.addAll(list)
            it.insertOrUpdate(obj)
        }
    }

    fun getMainUserWithRealm():MainUserRealmObject? = realm.where(MainUserRealmObject::class.java).findFirst()

    fun setMainUserToRealm(mainUserModel: MainUserModel){
        realm.executeTransactionAsync{
            it.insertOrUpdate(mainUserModelToRo(mainUserModel))
        }
    }

}
package com.example.test.data.network

import com.example.test.domain.models.ReposModel
import com.example.test.domain.models.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {
    @GET("users")
    suspend fun getUsers() : Response<ArrayList<UserModel>>

    @GET("users/{login}/repos")
    suspend fun getRepos(@Path("login") login:String):Response<ArrayList<ReposModel>>
}
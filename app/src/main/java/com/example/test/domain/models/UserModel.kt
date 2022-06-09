package com.example.test.domain.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") var id: Int,
    @SerializedName("login") var login: String,
    @SerializedName("node_id") var nodeId: String,
    @SerializedName("avatar_url") var avatarUrl: String,
    @SerializedName("repos_url") var reposUrl: String,
)

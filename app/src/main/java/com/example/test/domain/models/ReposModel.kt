package com.example.test.domain.models

import com.google.gson.annotations.SerializedName

data class ReposModel(
    @SerializedName("id") var id:Int,
    @SerializedName("name") var repo: String
)
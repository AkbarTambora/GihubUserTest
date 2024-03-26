package com.example.gihubusertest.api

import com.example.gihubusertest.data.model.DetailUserResponse
import com.example.gihubusertest.data.model.User
import com.example.gihubusertest.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowersDetail(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}
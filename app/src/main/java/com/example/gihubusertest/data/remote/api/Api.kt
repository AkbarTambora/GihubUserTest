package com.example.gihubusertest.data.remote.api

import com.example.gihubusertest.data.model.DetailUserResponse
import com.example.gihubusertest.data.model.User
import com.example.gihubusertest.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("search/users")
    fun getSearchUsersRep(
        @Query("q") query: String
    ): UserResponse

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/following")
    fun getFollowingDetail(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/followers")
    fun getFollowersDetail(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}
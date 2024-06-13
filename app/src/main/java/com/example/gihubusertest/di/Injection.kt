package com.example.gihubusertest.di

import android.content.Context
import com.example.gihubusertest.data.local.room.UserDatabase
import com.example.gihubusertest.data.remote.api.ApiConfig
import com.example.gihubusertest.data.remote.api.ApiConfig.getApiService
import com.example.gihubusertest.data.source.UserRepository
import com.example.gihubusertest.utils.AppExecutors


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}

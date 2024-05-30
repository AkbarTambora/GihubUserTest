package com.example.gihubusertest.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.data.local.room.UserDao
import com.example.gihubusertest.data.remote.api.Api
import com.example.gihubusertest.utils.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val apiService: Api,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<UserEntity>>>()

    fun getListUser(query: String = ""): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = withContext(Dispatchers.IO) {
                apiService.getSearchUsers(query).execute()
            }
            if (response.isSuccessful) {
                val users = response.body()?.items ?: emptyList()
                val usersList = users.map { user ->
                    val isBookmarked = userDao.isUserBookmarked(user.id)
                    UserEntity(
                        user.id,
                        user.login,
                        user.avatar_url,
                        isBookmarked
                    )
                }
                userDao.deleteAll()
                userDao.insertUsers(usersList)
                val localData: LiveData<Result<List<UserEntity>>> = userDao.getUsers().map { Result.Success(it) }
                emitSource(localData)
            } else {
                emit(Result.Error(response.message()))
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "getListUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getBookmarkedUsers(): LiveData<List<UserEntity>> {
        return userDao.getBookmarkedUsers()
    }

    suspend fun setBookmarkedUsers(user: UserEntity, bookmarkState: Boolean) {
        user.isBookmarked = bookmarkState
        userDao.updateUsers(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: Api,
            userDao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, appExecutors)
            }.also { instance = it }
    }
}

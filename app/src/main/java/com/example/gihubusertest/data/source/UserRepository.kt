package com.example.gihubusertest.data.source


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.gihubusertest.BuildConfig
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.data.local.room.UserDao
import com.example.gihubusertest.data.remote.api.ApiService
import com.example.gihubusertest.utils.AppExecutors

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<UserEntity>>>()

    fun getListUserFromApi(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getSearchUsers(query)
            val users = response.items
            val userList = users.map { user ->
                val isBookmarked = userDao.isUserBookmarked(user.id)
                UserEntity(
                    user.id,
                    user.login,
                    user.avatarUrl,
                    isBookmarked
                )
            }
            userDao.deleteAll()
            userDao.insertUsers(userList)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = userDao.getUsers().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getListUserFromLocal(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        val localData: LiveData<Result<List<UserEntity>>> = userDao.searchUsers(query).map { Result.Success(it) }
        emitSource(localData)
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
            apiService: ApiService,
            userDao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, appExecutors)
            }.also { instance = it }
    }
}



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

    fun getListUser(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getSearchUsers(BuildConfig.token)
            Log.d("Response", "Response Received")
            val users = response.items
            Log.d("Users", "Users Created from Response")
            val userList = users.map { users ->
                val isBookmarked = userDao.isUserBookmarked(users.id)
                UserEntity(
                    users.id,
                    users.login,
                    users.avatarUrl,
                    isBookmarked
                )
            }
            Log.d("List users", "Mapping the List to Entity")
            userDao.deleteAll()
            Log.d("Delete users", "users that Deleted who not bookmarked")
            userDao.insertUsers(userList)
            Log.d("Insert Users", "Users listed to Entity")
        } catch (e: Exception) {
            Log.d("User Repository", "getListUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = userDao.getUsers().map { Result.Success(it) }
        emitSource(localData)
        Log.d("LocalData", "User listed to Entity ditarik lewat DAO, lalu di mapping, lalu" +
                "dimasukkan ke data class Success, object, untuk dikirim ke Injection, UserViewModel" +
                "dan ViewModelFactory")
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

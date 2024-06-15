package com.example.gihubusertest.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gihubusertest.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY id DESC")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user WHERE bookmarked = 1")
    fun getBookmarkedUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user WHERE login LIKE :query")
    fun searchUsers(query: String): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUsers(user: UserEntity)

    @Query("DELETE FROM user WHERE bookmarked = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND bookmarked = 1)")
    suspend fun isUserBookmarked(id: Int): Boolean
}

package com.example.gihubusertest.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gihubusertest.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY id DESC")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user where bookmarked = 1")
    fun getBookmarkedUsers(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUsers(User: UserEntity)

    @Query("DELETE FROM user WHERE bookmarked = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND bookmarked = 1)")
    suspend fun isUsersBookmarked(id: Int): Boolean
}
package com.example.gihubusertest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.data.source.UserRepository
import com.example.gihubusertest.data.source.Result
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<Result<List<UserEntity>>>()
    val users: LiveData<Result<List<UserEntity>>> = _users

    private val _favoriteStatusChanged = MutableLiveData<UserEntity>()
    val favoriteStatusChanged: LiveData<UserEntity> = _favoriteStatusChanged

    fun setSearchUsers(query: String) {
        viewModelScope.launch {
            userRepository.getListUser(query).observeForever {
                _users.postValue(it)
            }
        }
    }

    fun getBookmarkedUsers(): LiveData<List<UserEntity>> = userRepository.getBookmarkedUsers()

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setBookmarkedUsers(user, true)
            _favoriteStatusChanged.postValue(user.copy(isBookmarked = true))
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setBookmarkedUsers(user, false)
            _favoriteStatusChanged.postValue(user.copy(isBookmarked = false))
        }
    }

    fun getListUser(query: String): LiveData<Result<List<UserEntity>>> {
        return userRepository.getListUser(query)
    }
}
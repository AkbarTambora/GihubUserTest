package com.example.gihubusertest.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.data.source.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getListUser(query: String) = userRepository.getListUser(query)

    fun getBookmarkedUsers() = userRepository.getBookmarkedUsers()

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setBookmarkedUsers(user, true)
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setBookmarkedUsers(user, false)
        }
    }
}

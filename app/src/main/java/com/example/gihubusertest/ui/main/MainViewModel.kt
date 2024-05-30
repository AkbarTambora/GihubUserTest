package com.example.gihubusertest.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gihubusertest.data.local.entity.UserEntity
import com.example.gihubusertest.data.model.UserResponse
import com.example.gihubusertest.data.remote.api.RetrofitClient
import com.example.gihubusertest.data.source.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _listUsers = MutableLiveData<List<UserEntity>>()
    val listUsers: LiveData<List<UserEntity>> = _listUsers

    fun setSearchUsers(query: String) {
        RetrofitClient.apiInstance.getSearchUsers(query).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()?.items?.map { user ->
                        UserEntity(user.id, user.login, user.avatar_url, false)
                    } ?: emptyList()
                    _listUsers.postValue(users)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Failure", t.message ?: "Unknown error")
            }
        })
    }

    fun setBookmarkedUsers(user: UserEntity, bookmarkState: Boolean) {
        viewModelScope.launch {
            userRepository.setBookmarkedUsers(user, bookmarkState)
        }
    }
}

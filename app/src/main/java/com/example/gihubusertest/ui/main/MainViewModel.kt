package com.example.gihubusertest.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gihubusertest.data.remote.api.RetrofitClient
import com.example.gihubusertest.data.model.User
import com.example.gihubusertest.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<ArrayList<User>>()
    val listUsers: LiveData<ArrayList<User>> = _listUsers

    fun setSearchUsers(query: String) {
        RetrofitClient.apiInstance.getSearchUsers(query).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Failure", t.message ?: "Unknown error")
            }
        })
    }
}
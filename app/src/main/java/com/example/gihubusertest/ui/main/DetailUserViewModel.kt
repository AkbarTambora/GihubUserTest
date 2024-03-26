package com.example.gihubusertest.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gihubusertest.api.RetrofitClient
import com.example.gihubusertest.data.model.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {
    private val _users = MutableLiveData<DetailUserResponse>()

    fun setUsersDetail(username: String) {
        RetrofitClient.apiInstance.getUserDetail(username).enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                if (response.isSuccessful) {
                    _users.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.d("Failure", t.message ?: "Unknown error")
            }
        })
    }

    fun getUsersDetail(): LiveData<DetailUserResponse> {
        return _users
    }
}
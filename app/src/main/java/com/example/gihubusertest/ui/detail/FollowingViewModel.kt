package com.example.gihubusertest.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gihubusertest.api.RetrofitClient
import com.example.gihubusertest.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {

    private val _listFollowing = MutableLiveData<ArrayList<User>>()
    val listFollowing: LiveData<ArrayList<User>> = _listFollowing

    fun setListFollowing(username: String) {
        RetrofitClient.apiInstance.getFollowingDetail(username)
            .enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if (response.isSuccessful) {
                    _listFollowing.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.d("Failure", t.message ?: "Unknown error")
            }
        })
    }

    fun getUsersDetail(): LiveData<ArrayList<User>> {
        return listFollowing
    }
}
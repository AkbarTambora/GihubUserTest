package com.example.gihubusertest.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gihubusertest.data.model.User
import com.example.gihubusertest.data.remote.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _listFollowing = MutableLiveData<List<User>>()
    val listFollowing: LiveData<List<User>> = _listFollowing

    fun setListFollowing(username: String) {
        ApiConfig.getApiService().getFollowingDetail(username)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        _listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.d("Failure", t.message ?: "Unknown error")
                }
            })
    }
}

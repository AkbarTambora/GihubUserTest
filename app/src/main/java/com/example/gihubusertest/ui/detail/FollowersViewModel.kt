package com.example.gihubusertest.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gihubusertest.data.model.User
import com.example.gihubusertest.data.remote.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _listFollowers = MutableLiveData<List<User>>()
    val listFollowers: LiveData<List<User>> = _listFollowers

    fun setListFollowers(username: String) {
        RetrofitClient.apiInstance.getFollowersDetail(username)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        _listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.d("Failure", t.message ?: "Unknown error")
                }
            })
    }
}

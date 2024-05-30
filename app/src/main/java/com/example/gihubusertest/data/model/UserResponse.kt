package com.example.gihubusertest.data.model

data class UserResponse(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<User>
)

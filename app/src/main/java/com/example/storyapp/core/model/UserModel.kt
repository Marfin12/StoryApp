package com.example.storyapp.core.model

data class UserModel(
    val id: String,
    val name: String,
    val token: String,
    val isLogin: Boolean,
)
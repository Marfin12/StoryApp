package com.example.storyapp.network


data class ApiResponse<T>(
    var message: String? = null,
    var apiStatus: ApiStatus? = null,
    var data: T? = null
)
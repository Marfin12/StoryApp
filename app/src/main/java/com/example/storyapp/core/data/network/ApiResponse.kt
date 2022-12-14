package com.example.storyapp.core.data.network


data class ApiResponse<T>(
    var message: String? = null,
    var apiStatus: ApiStatus? = null,
    var data: T? = null
)
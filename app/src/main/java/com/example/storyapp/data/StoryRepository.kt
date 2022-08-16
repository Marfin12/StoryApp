package com.example.storyapp.data

import com.example.storyapp.network.ApiService
import com.example.storyapp.response.StoryResponse
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.RegisterResponse


class StoryRepository(private val apiService: ApiService) {
    suspend fun getStoryList(token: String): StoryResponse {
        return apiService.getStoryList("Bearer $token")
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            apiService.login(email, password)
        } catch(ex: Exception) {
            LoginResponse(
                error = true,
                message = ex.message!!,
                loginResult = null
            )
        }
    }

    suspend fun register(
        email: String, password: String, name: String
    ): RegisterResponse {
        return try {
            apiService.register(email, password, name)
        } catch(ex: Exception) {
            RegisterResponse(
                error = true,
                message = ex.message!!
            )
        }
    }
}
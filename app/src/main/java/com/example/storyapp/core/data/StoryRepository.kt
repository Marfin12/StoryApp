package com.example.storyapp.core.data

import com.example.storyapp.core.EMPTY_STRING
import com.example.storyapp.core.data.network.ApiService
import com.example.storyapp.core.data.response.FileUploadResponse
import com.example.storyapp.core.data.response.StoryResponse
import com.example.storyapp.core.data.response.LoginResponse
import com.example.storyapp.core.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


class StoryRepository(private val apiService: ApiService) {
    suspend fun getStoryList(token: String): StoryResponse {
        return try {
            apiService.getStoryList("Bearer $token")
        } catch(ex: Exception) {
            StoryResponse(
                listOf(),
                isError = true,
                message = ex.message ?: EMPTY_STRING
            )
        }
    }

    suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): FileUploadResponse {
        return try {
            apiService.uploadImage("Bearer $token", file, description)
        } catch(ex: Exception) {
            FileUploadResponse(
                error = true,
                message = ex.message ?: EMPTY_STRING
            )
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            apiService.login(email, password)
        } catch(ex: Exception) {
            LoginResponse(
                error = true,
                message = ex.message ?: EMPTY_STRING,
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
                message = ex.message ?: EMPTY_STRING
            )
        }
    }
}
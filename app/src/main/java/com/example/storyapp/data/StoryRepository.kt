package com.example.storyapp.data

import com.example.storyapp.network.ApiService
import com.example.storyapp.response.ListStoryResponse


class StoryRepository(private val apiService: ApiService) {
    suspend fun getStoryList(): ListStoryResponse {
        return apiService.getStoryList()
    }
}
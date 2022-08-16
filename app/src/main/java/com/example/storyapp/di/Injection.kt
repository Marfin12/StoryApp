package com.example.storyapp.di

import com.example.storyapp.data.StoryRepository
import com.example.storyapp.network.ApiConfig

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}
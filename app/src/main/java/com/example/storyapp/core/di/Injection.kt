package com.example.storyapp.core.di

import com.example.storyapp.core.data.StoryRepository
import com.example.storyapp.core.data.network.ApiConfig

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}
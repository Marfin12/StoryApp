package com.example.storyapp.main

import androidx.lifecycle.*
import com.example.storyapp.core.data.StoryRepository
import com.example.storyapp.core.data.network.ApiResponse
import com.example.storyapp.core.data.network.ApiStatus
import com.example.storyapp.core.getApiResponse
import com.example.storyapp.core.mapResponsesToStoryModel
import com.example.storyapp.core.model.StoryModel
import com.example.storyapp.core.model.UserModel
import com.example.storyapp.core.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _stories = MutableLiveData<ApiResponse<List<StoryModel>>>()
    var stories: LiveData<ApiResponse<List<StoryModel>>> = _stories

    fun getStories(token: String) {
        _stories.value = getApiResponse(null, ApiStatus.LOADING, listOf())
        viewModelScope.launch {
            try {
                val response = storyRepository.getStoryList(token)
                when {
                    response.storyResults.isEmpty() -> {
                        _stories.value = getApiResponse(response.message, ApiStatus.EMPTY, listOf())
                    }
                    response.isError -> {
                        _stories.value = getApiResponse(response.message, ApiStatus.ERROR, listOf())
                    }
                    else -> {
                        val listStory = mapResponsesToStoryModel(response.storyResults)
                        _stories.value =
                            getApiResponse(response.message, ApiStatus.SUCCESS, listStory)
                    }
                }
            } catch (ex: Exception) {
                _stories.value = getApiResponse(ex.message, ApiStatus.ERROR, listOf())
            }
        }
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}
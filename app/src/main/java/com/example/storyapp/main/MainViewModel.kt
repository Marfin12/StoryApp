package com.example.storyapp.main

import androidx.lifecycle.*
import com.example.storyapp.DataMapper
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.StoryModel
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiResponse
import com.example.storyapp.response.ListStoryResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
    ) : ViewModel() {
    private val _message = MutableLiveData<String>()
    var message: LiveData<String> = _message

    private val _status = MutableLiveData<ApiResponse>()
    var status: LiveData<ApiResponse> = _status

    private val _stories = MutableLiveData<List<StoryModel>>()
    var stories: LiveData<List<StoryModel>> = _stories

    fun getStories() {
        _message.value = ""
        _status.value = ApiResponse.LOADING
        viewModelScope.launch {
            try{
                val response = storyRepository.getStoryList()
                when {
                    response.stories.isEmpty() -> {
                        _status.value = ApiResponse.EMPTY
                        _message.value = response.message
                    }
                    response.isError -> {
                        _status.value = ApiResponse.ERROR
                        _message.value = response.message
                    }
                    else -> {
                        val listStory = DataMapper.mapResponsesToStoryModel(response.stories)
                        _stories.postValue(listStory)
                        _status.value = ApiResponse.SUCCESS
                    }
                }
            }
            catch (ex: Exception) {
                _status.value = ApiResponse.ERROR
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
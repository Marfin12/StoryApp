package com.example.storyapp.main

import androidx.lifecycle.*
import com.example.storyapp.DataMapper
import com.example.storyapp.EMPTY_STRING
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.StoryModel
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiStatus
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
    ) : ViewModel() {
    private val _message = MutableLiveData<String>()
    var message: LiveData<String> = _message

    private val _status = MutableLiveData<ApiStatus>()
    var status: LiveData<ApiStatus> = _status

    private val _stories = MutableLiveData<List<StoryModel>>()
    var stories: LiveData<List<StoryModel>> = _stories

    fun getStories(token: String) {
        _message.value = EMPTY_STRING
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try{
                val response = storyRepository.getStoryList(token)
                when {
                    response.storyResults.isEmpty() -> {
                        _status.value = ApiStatus.EMPTY
                        _message.value = response.message
                    }
                    response.isError -> {
                        _status.value = ApiStatus.ERROR
                        _message.value = response.message
                    }
                    else -> {
                        val listStory = DataMapper.mapResponsesToStoryModel(response.storyResults)
                        _stories.postValue(listStory)
                        _status.value = ApiStatus.SUCCESS
                    }
                }
            }
            catch (ex: Exception) {
                _status.value = ApiStatus.ERROR
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
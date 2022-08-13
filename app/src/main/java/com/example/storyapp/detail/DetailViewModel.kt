package com.example.storyapp.detail

import androidx.lifecycle.*
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiResponse
import com.example.storyapp.response.ListStoryResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel(
    private val pref: UserPreference
    ) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

}
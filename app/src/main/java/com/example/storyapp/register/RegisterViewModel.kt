package com.example.storyapp.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.getApiResponse
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiResponse
import com.example.storyapp.network.ApiStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val storyRepository: StoryRepository
    ) : ViewModel() {
    private val _status = MutableLiveData<ApiResponse>()
    var status: LiveData<ApiResponse> = _status

    fun register(name: String, email: String, password: String) {
        _status.value = getApiResponse(null, ApiStatus.LOADING)

        viewModelScope.launch {
            delay(1000)
            val resp = storyRepository.register(email, password, name)

            if (resp.error) {
                _status.value = getApiResponse(resp.message, ApiStatus.ERROR)
            } else {
                _status.value = getApiResponse(resp.message, ApiStatus.SUCCESS)
            }
        }
    }
}
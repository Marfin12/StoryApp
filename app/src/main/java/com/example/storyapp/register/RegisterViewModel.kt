package com.example.storyapp.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.core.data.StoryRepository
import com.example.storyapp.core.data.network.ApiResponse
import com.example.storyapp.core.data.network.ApiStatus
import com.example.storyapp.core.getApiResponse
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _status = MutableLiveData<ApiResponse<Any>>()
    var status: LiveData<ApiResponse<Any>> = _status

    fun register(name: String, email: String, password: String) {
        _status.value = getApiResponse(null, ApiStatus.LOADING)

        viewModelScope.launch {
            val resp = storyRepository.register(email, password, name)

            if (resp.error) {
                _status.value = getApiResponse(resp.message, ApiStatus.ERROR)
            } else {
                _status.value = getApiResponse(resp.message, ApiStatus.SUCCESS)
            }
        }
    }
}
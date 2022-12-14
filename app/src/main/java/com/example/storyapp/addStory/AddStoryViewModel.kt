package com.example.storyapp.addStory

import androidx.lifecycle.*
import com.example.storyapp.core.data.StoryRepository
import com.example.storyapp.core.model.UserModel
import com.example.storyapp.core.model.UserPreference
import com.example.storyapp.core.data.network.ApiResponse
import com.example.storyapp.core.data.network.ApiStatus
import com.example.storyapp.core.getApiResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _uploadStatus = MutableLiveData<ApiResponse<Any>>()
    var uploadStatus: LiveData<ApiResponse<Any>> = _uploadStatus

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        _uploadStatus.value = getApiResponse(null, ApiStatus.LOADING)

        viewModelScope.launch {
            delay(1000)
            val resp = storyRepository.uploadStory(token, file, description)
            if (resp.error) {
                _uploadStatus.value = getApiResponse(resp.message, ApiStatus.ERROR)
            } else {
                _uploadStatus.value = getApiResponse(resp.message, ApiStatus.SUCCESS)
            }
        }
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}
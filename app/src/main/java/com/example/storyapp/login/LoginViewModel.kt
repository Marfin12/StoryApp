package com.example.storyapp.login

import androidx.lifecycle.*
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.getApiResponse
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiResponse
import com.example.storyapp.network.ApiStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
    ) : ViewModel() {
    private val _status = MutableLiveData<ApiResponse>()
    var status: LiveData<ApiResponse> = _status

    fun login(email: String, password: String) {
        _status.value = getApiResponse(null, ApiStatus.LOADING)

        viewModelScope.launch {
            delay(1000)
            val resp = storyRepository.login(email, password)
            if (resp.error) {
                _status.value = getApiResponse(resp.message, ApiStatus.ERROR)
            } else {
                if (resp.loginResult != null) {
                    val userModel = UserModel(
                        resp.loginResult.userId,
                        resp.loginResult.name,
                        resp.loginResult.token,
                        true,
                    )
                    pref.saveUser(userModel)
                    _status.value = getApiResponse(resp.message, ApiStatus.SUCCESS)
                } else {
                    _status.value = getApiResponse(resp.message, ApiStatus.ERROR)
                }
            }
        }
    }
}
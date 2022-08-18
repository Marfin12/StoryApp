package com.example.storyapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.core.data.StoryRepository
import com.example.storyapp.core.data.network.ApiResponse
import com.example.storyapp.core.data.network.ApiStatus
import com.example.storyapp.core.getApiResponse
import com.example.storyapp.core.model.UserModel
import com.example.storyapp.core.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _status = MutableLiveData<ApiResponse<Any>>()
    var status: LiveData<ApiResponse<Any>> = _status

    fun login(email: String, password: String) {
        _status.value = getApiResponse(null, ApiStatus.LOADING)

        viewModelScope.launch {
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
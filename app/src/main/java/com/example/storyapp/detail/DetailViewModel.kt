package com.example.storyapp.detail

import androidx.lifecycle.*
import com.example.storyapp.core.model.UserModel
import com.example.storyapp.core.model.UserPreference

class DetailViewModel(
    private val pref: UserPreference
) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

}
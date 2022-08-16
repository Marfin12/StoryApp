package com.example.storyapp.detail

import androidx.lifecycle.*
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference

class DetailViewModel(
    private val pref: UserPreference
    ) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

}
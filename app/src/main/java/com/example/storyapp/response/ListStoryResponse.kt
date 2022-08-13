package com.example.storyapp.response

import com.google.gson.annotations.SerializedName

data class ListStoryResponse(
    @field:SerializedName("listStory")
    val stories: List<StoryResponse>,

    @field:SerializedName("error")
    val isError: Boolean,

    @field:SerializedName("message")
    val message: String
)

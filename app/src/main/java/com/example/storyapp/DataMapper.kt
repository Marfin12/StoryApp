package com.example.storyapp

import com.example.storyapp.model.StoryModel
import com.example.storyapp.response.StoryResultResponse


object DataMapper {
    fun mapResponsesToStoryModel(input: List<StoryResultResponse>): List<StoryModel> {
        val storyList = ArrayList<StoryModel>()
        input.map {
            val story = StoryModel(
                id = it.id,
                name = it.name,
                description = it.description,
                photoUrl = it.photoUrl,
            )
            storyList.add(story)
        }

        return storyList
    }
}
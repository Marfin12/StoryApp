package com.example.storyapp.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryModel(
    var id: String,
    var name: String,
    var description: String,
    var photoUrl: String
) : Parcelable
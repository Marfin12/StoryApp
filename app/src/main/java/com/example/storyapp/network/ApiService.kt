package com.example.storyapp.network

import com.example.storyapp.response.FileUploadResponse
import com.example.storyapp.response.ListStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
   @GET("stories")
   suspend fun getStoryList(): ListStoryResponse

   @Multipart
   @POST("stories")
   fun uploadImage(
      @Part file: MultipartBody.Part,
      @Part("description") description: RequestBody,
   ): Call<FileUploadResponse>
}

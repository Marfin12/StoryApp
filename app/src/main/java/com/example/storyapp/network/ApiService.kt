package com.example.storyapp.network

import com.example.storyapp.response.FileUploadResponse
import com.example.storyapp.response.StoryResponse
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

   @GET("stories")
   suspend fun getStoryList(
      @Header("Authorization") token: String
   ): StoryResponse

   @Multipart
   @POST("stories")
   fun uploadImage(
      @Part file: MultipartBody.Part,
      @Part("description") description: RequestBody,
   ): Call<FileUploadResponse>

   @FormUrlEncoded
   @POST("login")
   suspend fun login(
      @Field("email") email: String,
      @Field("password") password: String
   ): LoginResponse

   @FormUrlEncoded
   @POST("register")
   suspend fun register(
      @Field("email") email: String,
      @Field("password") password: String,
      @Field("name") name: String
   ): RegisterResponse
}

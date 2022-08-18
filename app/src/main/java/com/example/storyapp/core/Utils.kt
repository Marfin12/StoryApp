package com.example.storyapp.core

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.core.model.StoryModel
import com.example.storyapp.core.data.network.ApiResponse
import com.example.storyapp.core.data.network.ApiStatus
import com.example.storyapp.core.data.response.StoryResultResponse
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)

    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)

    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}

fun getApiResponse(message: String?, apiStatus: ApiStatus): ApiResponse<Any> {
    return ApiResponse(
        message,
        apiStatus,
        null
    )
}

fun getApiResponse(
    message: String?, apiStatus: ApiStatus, data: List<StoryModel>
): ApiResponse<List<StoryModel>> {
    return ApiResponse(
        message,
        apiStatus,
        data
    )
}

fun showMessageDialog(title: Int, message: Int, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun showMessageDialog(title: Int, message: String, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

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
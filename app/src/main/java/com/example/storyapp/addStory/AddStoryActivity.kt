package com.example.storyapp.addStory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.*
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.main.MainActivity
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiStatus
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var addStoryViewModel: AddStoryViewModel

    private var getFile: File? = null
    private var token = ""

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.camera_permission_access_failed),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupComponent()
        setupPermission()
    }

    private fun setupViewModel() {
        addStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AddStoryViewModel::class.java]

        addStoryViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                token = user.token
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        addStoryViewModel.uploadStatus.observe(this) { uploadStatus ->
            when(uploadStatus.apiStatus) {
                ApiStatus.LOADING -> {
                    binding.loadingUploadStory.loadingMotion.transitionToStart()
                    binding.loadingUploadStory.root.visibility = View.VISIBLE
                }
                ApiStatus.SUCCESS -> {
                    binding.loadingUploadStory.root.visibility = View.GONE

                    val intent = Intent(this, MainActivity::class.java)
                    val otherFlags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and otherFlags)
                    startActivity(intent)
                    finish()
                }
                ApiStatus.ERROR -> {
                    binding.loadingUploadStory.root.visibility = View.GONE

                    Toast.makeText(
                        this@AddStoryActivity,
                        uploadStatus.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    binding.loadingUploadStory.root.visibility = View.GONE

                    Toast.makeText(
                        this@AddStoryActivity,
                        R.string.error_try_again_later,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupComponent() {
        binding.cameraButton.setOnClickListener {
            startTakePhoto()
        }
        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.uploadButton.setOnClickListener {
            val description = binding.descriptionEditText.text.toString()
            if (isValidateUploadButton(description))
                uploadStory(description)
        }
    }

    private fun setupPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun uploadStory(desc: String) {
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        addStoryViewModel.uploadStory(token, imageMultipart, desc.toRequestBody("text/plain".toMediaType()))
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.camera_choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun isValidateUploadButton(desc: String): Boolean {
        if (getFile == null) {
            Toast.makeText(
                this@AddStoryActivity,
                getString(R.string.camera_please_input_image_file),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (desc.isEmpty()) {
            Toast.makeText(
                this@AddStoryActivity,
                getString(R.string.camera_please_input_description),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}
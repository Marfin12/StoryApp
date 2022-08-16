package com.example.storyapp.register

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiStatus
import com.example.storyapp.showMessageDialog

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupComponent()
        setupViewModel()
        setupAction()
    }

    private fun setupComponent() {
        binding.loadingCommon.imageView.setImageDrawable(ContextCompat.getDrawable(
            this, R.drawable.icon_story_app_register
        ))
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.status.observe(this) { apiResponse ->
            when(apiResponse.apiStatus) {
                ApiStatus.LOADING -> {
                    binding.loadingCommon.loadingMotion.transitionToStart()
                    binding.loadingCommon.root.visibility = View.VISIBLE
                }
                ApiStatus.SUCCESS -> {
                    binding.loadingCommon.root.visibility = View.GONE
                    finish()
                }
                ApiStatus.ERROR -> {
                    binding.loadingCommon.root.visibility = View.GONE
                    if (apiResponse.message != null) {
                        showMessageDialog(
                            R.string.failed_register_title,
                            apiResponse.message!!,
                            this@RegisterActivity
                        )
                    }
                }
                else -> {
                    binding.loadingCommon.root.visibility = View.GONE
                    showMessageDialog(
                        R.string.failed_register_title,
                        R.string.error_try_again_later,
                        this@RegisterActivity
                    )
                }
            }
        }
    }

    private fun setupAction() {
        with(binding) {

            registerButton.setOnClickListener {
                if (customRegisterApp.isValidRegister()) {
                    with(customRegisterApp) {
                        registerViewModel.register(
                            usernameEditText.text.toString(),
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )
                    }
                } else {
                    showMessageDialog(
                        R.string.failed_register_title,
                        R.string.failed_register_description,
                        this@RegisterActivity
                    )
                }
            }

        }
    }
}
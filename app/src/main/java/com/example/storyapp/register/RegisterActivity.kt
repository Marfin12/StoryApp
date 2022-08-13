package com.example.storyapp.register

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        with(binding) {
            registerButton.setOnClickListener {
                with(customRegisterApp) {
                    if (customTextViewErrorLayout.visibility != View.VISIBLE) {
                        register(
                            usernameEditText.text.toString(),
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )
                    }
                }
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        registerViewModel.saveUser(UserModel(name, email, password, false))
    }
}
package com.example.storyapp.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.main.MainActivity
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiStatus
import com.example.storyapp.register.RegisterActivity
import com.example.storyapp.showMessageDialog

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.status.observe(this) { apiResponse ->
            when(apiResponse.apiStatus) {
                ApiStatus.LOADING -> {
                    binding.loadingCommon.loadingMotion.transitionToStart()
                    binding.loadingCommon.root.visibility = View.VISIBLE
                }
                ApiStatus.SUCCESS -> {
                    binding.loadingCommon.root.visibility = View.GONE

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                ApiStatus.ERROR -> {
                    binding.loadingCommon.root.visibility = View.GONE
                    if (apiResponse.message != null) {
                        showMessageDialog(
                            R.string.failed_login_title,
                            apiResponse.message!!,
                            this@LoginActivity
                        )
                    }
                }
                else -> {
                    binding.loadingCommon.root.visibility = View.GONE
                    showMessageDialog(
                        R.string.failed_login_title,
                        R.string.error_try_again_later,
                        this@LoginActivity
                    )
                }
            }
        }
    }

    private fun setupAction() {
        with(binding) {

            loginButton.setOnClickListener {
                if (customLoginApp.isValidLogin()) {
                    with(customLoginApp) {
                        loginViewModel.login(
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )
                    }
                } else {
                    showMessageDialog(
                        R.string.failed_login_title,
                        R.string.failed_login_description,
                        this@LoginActivity
                    )
                }
            }

            registerButton.setOnClickListener {
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
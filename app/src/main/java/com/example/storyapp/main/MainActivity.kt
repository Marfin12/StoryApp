package com.example.storyapp.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.addStory.AddStoryActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.model.UserPreference
import com.example.storyapp.network.ApiResponse

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var animStoriesItem = AnimatorSet()
    private var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[MainViewModel::class.java]

        initUserViewModel()
        initStoryViewModel()
        playAnimation()
    }

    private fun initUserViewModel() {
        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                supportActionBar?.title = user.name
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun initStoryViewModel() {
        mainViewModel.getStories()

        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter

        mainViewModel.stories.observe(this) {
            adapter.submitList(it)
        }

        mainViewModel.message.observe(this) {
            message = it
        }

        mainViewModel.status.observe(this) {
            when(it) {
                ApiResponse.LOADING -> {
                    binding.incLoadingStory.root.visibility = View.VISIBLE
                    binding.rvStory.visibility = View.GONE
                }
                ApiResponse.SUCCESS -> {
                    animStoriesItem.end()
                    binding.incLoadingStory.root.visibility = View.GONE
                    binding.rvStory.visibility = View.VISIBLE
                }
                ApiResponse.EMPTY -> {
                    animStoriesItem.end()
                    binding.incLoadingStory.root.visibility = View.GONE
                    binding.incEmptyStory.root.visibility = View.VISIBLE
                    binding.incEmptyStory.errorMessageTextView.text = message
                }
                ApiResponse.ERROR -> {
                    onStoriesError()
                }
                else -> {
                    onStoriesError()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                mainViewModel.logout()
            }
            R.id.addStoryBook -> {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }
        }

        return true
    }

    private fun onStoriesError() {
        animStoriesItem.end()
        binding.incLoadingStory.root.visibility = View.GONE
        binding.incEmptyStory.root.visibility = View.VISIBLE
        if (message.isEmpty()) {
            binding.incEmptyStory.errorMessageTextView.text = this.getString(R.string.error_story_list)
        } else {
            binding.incEmptyStory.errorMessageTextView.text = message
        }
    }

    private fun playAnimation() {
        with(binding.incLoadingStory) {
            val templateCardExpand2 = ObjectAnimator.ofFloat(
                templateCard2, View.TRANSLATION_Y, 0f, 40f
            ).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
            val templateCardExpand3 = ObjectAnimator.ofFloat(
                templateCard3, View.TRANSLATION_Y, 0f, 80f
            ).apply {
                duration = 4000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

            val templateCardAlphaAnim1 =
                ObjectAnimator.ofFloat(templateCard1, View.ALPHA, 1f).setDuration(500)
            val templateCardAlphaAnim2 =
                ObjectAnimator.ofFloat(templateCard2, View.ALPHA, 1f).setDuration(500)
            val templateCardAlphaAnim3 =
                ObjectAnimator.ofFloat(templateCard3, View.ALPHA, 1f).setDuration(500)

            animStoriesItem = AnimatorSet().apply {
                playTogether(
                    templateCardExpand2, templateCardExpand3,
                    templateCardAlphaAnim1, templateCardAlphaAnim2, templateCardAlphaAnim3
                )
                startDelay = 500
            }
            animStoriesItem.start()
        }
    }
}
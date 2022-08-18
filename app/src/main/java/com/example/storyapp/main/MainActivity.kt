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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.addStory.AddStoryActivity
import com.example.storyapp.core.EMPTY_STRING
import com.example.storyapp.core.STORY
import com.example.storyapp.core.data.network.ApiStatus
import com.example.storyapp.core.model.StoryModel
import com.example.storyapp.core.model.UserPreference
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.detail.DetailActivity
import com.example.storyapp.login.LoginActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
const val KEY_STORY_LIST = "story_list_key"

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var animStoriesItem = AnimatorSet()
    private var message = EMPTY_STRING
    private var isAlreadyLoaded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            isAlreadyLoaded = savedInstanceState.getBoolean(KEY_STORY_LIST, true)
        }

        setupComponent()
        setupViewModel()
    }

    private fun setupComponent() {
        binding.root.visibility = View.INVISIBLE
        actionBar?.hide()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        initUserViewModel()
        initStoryViewModel()
        initAnimation()
    }

    private fun initUserViewModel() {
        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                binding.root.visibility = View.VISIBLE
                actionBar?.show()
                supportActionBar?.title = user.name


                if (isAlreadyLoaded) {
                    mainViewModel.getStories(user.token)
                    isAlreadyLoaded = false
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun initStoryViewModel() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter

        mainViewModel.stories.observe(this) { storyResponse ->
            when (storyResponse.apiStatus) {
                ApiStatus.LOADING -> {
                    animStoriesItem.start()

                    binding.incLoadingStory.root.visibility = View.VISIBLE
                    binding.rvStory.visibility = View.GONE
                }
                ApiStatus.SUCCESS -> {
                    animStoriesItem.end()

                    adapter.submitList(storyResponse.data)
                    adapter.onItemClick = { story, optionsCompat ->
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra(STORY, story)

                        startActivity(intent, optionsCompat.toBundle())
                    }

                    listStories = storyResponse.data ?: listOf()

                    binding.incLoadingStory.root.visibility = View.GONE
                    binding.rvStory.visibility = View.VISIBLE
                }
                ApiStatus.EMPTY -> {
                    animStoriesItem.end()
                    binding.incLoadingStory.root.visibility = View.GONE
                    binding.incEmptyStory.root.visibility = View.VISIBLE
                    binding.incEmptyStory.errorMessageTextView.text = storyResponse.message
                }
                ApiStatus.ERROR -> {
                    onStoriesError()
                }
                else -> {
                    onStoriesError()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(KEY_STORY_LIST, isAlreadyLoaded)
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
                listStories = listOf()
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
            binding.incEmptyStory.errorMessageTextView.text =
                this.getString(R.string.error_try_again_later)
        } else {
            binding.incEmptyStory.errorMessageTextView.text = message
        }
    }

    private fun initAnimation() {
        with(binding.incLoadingStory) {
            val templateCardExpand2 = ObjectAnimator.ofFloat(
                templateCard2, View.TRANSLATION_Y, 0f, 440f
            ).apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
            val templateCardExpand3 = ObjectAnimator.ofFloat(
                templateCard3, View.TRANSLATION_Y, 0f, 880f
            ).apply {
                duration = 3000
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
        }
    }

    companion object {
        var listStories: List<StoryModel> = listOf()
    }
}
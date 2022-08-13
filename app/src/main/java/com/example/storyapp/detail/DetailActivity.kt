package com.example.storyapp.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.storyapp.R
import com.example.storyapp.STORY
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.model.StoryModel
import com.example.storyapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupData()
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[DetailViewModel::class.java]

        detailViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                supportActionBar?.title = user.name
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<StoryModel>(STORY) as StoryModel
        Glide.with(applicationContext)
            .load(story.photoUrl)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    binding.storyImageView.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    binding.storyImageView.setBackgroundResource(R.drawable.bg_image_not_loaded)
                    super.onLoadFailed(errorDrawable)
                }
            })
        findViewById<TextView>(R.id.nameTextView).text = story.name
        findViewById<TextView>(R.id.descTextView).text = story.description
    }
}
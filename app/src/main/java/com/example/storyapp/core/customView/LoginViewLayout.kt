package com.example.storyapp.core.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.storyapp.R
import com.example.storyapp.core.EMPTY_STRING

class LoginViewLayout : LinearLayout {

    val emailEditText = CustomEmailEditText(context)
    val passwordEditText = CustomPasswordEditText(context)
    val imageView = ImageView(context)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setupLayout()

        addStoryAppLogo()

        addView(emailEditText)
        addView(passwordEditText)
    }

    private fun setupLayout() {
        val myParam = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        layoutParams = myParam
        orientation = VERTICAL
    }

    private fun addStoryAppLogo() {
        val storyImageView = ImageView(context)
        val myParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        myParams.setMargins(80, 40, 0, 0)

        storyImageView.layoutParams = myParams
        storyImageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.icon_story_app_login
            ) as Drawable
        )

        addView(storyImageView)
    }

    fun isValidLogin(): Boolean =
        (emailEditText.text ?: EMPTY_STRING.isNotEmpty()) as Boolean
                && (passwordEditText.text ?: EMPTY_STRING.isNotEmpty()) as Boolean
                && isNoErrorForAllEditText()

    private fun isNoErrorForAllEditText(): Boolean {
        return emailEditText.error == null && passwordEditText.error == null
    }
}
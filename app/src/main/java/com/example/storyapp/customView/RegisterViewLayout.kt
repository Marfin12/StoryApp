package com.example.storyapp.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.example.storyapp.R

class RegisterViewLayout : CustomViewLayout {

    val usernameEditText = EditText(context)
    val emailEditText = EditText(context)
    val passwordEditText = EditText(context)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setupLayout()

        addStoryAppLogo(
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        )
        addCommonEditText(
            usernameEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ), context.getString(R.string.hint_username)
        )
        addCommonEditText(
            emailEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ), context.getString(R.string.hint_email)
        )
        addPasswordEditText(
            passwordEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ),
            context.getString(R.string.hint_password)
        )
    }
}
package com.example.storyapp.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.example.storyapp.R

class LoginViewLayout : CustomViewLayout {

    private val usernameEditText = EditText(context)
    private val passwordEditText = EditText(context)

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
            editText = usernameEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ), context.getString(R.string.hint_email)
        )
        addPasswordEditText(
            editText = passwordEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ),
            context.getString(R.string.hint_password)
        )
    }
}
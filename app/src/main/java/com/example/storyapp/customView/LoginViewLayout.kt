package com.example.storyapp.customView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.example.storyapp.R

class LoginViewLayout : CustomViewLayout {

    val emailEditText = EditText(context)
    val passwordEditText = EditText(context)
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

        addStoryAppLogo(
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        )
        addCommonEditText(
            editText = emailEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ), context.getString(R.string.hint_email),
            { editText, context, customTextViewErrorLayout, s ->
                Utils.onValidateEmail(
                    editText, context, customTextViewErrorLayout, s
                )
            }
        )
        addPasswordEditText(passwordEditText)
        addCustomTextViewErrorLayout()
    }

    fun isValidLogin(): Boolean =
        emailEditText.text.isNotEmpty()
                && passwordEditText.text.isNotEmpty()
                && customTextViewErrorLayout.visibility != View.VISIBLE
}
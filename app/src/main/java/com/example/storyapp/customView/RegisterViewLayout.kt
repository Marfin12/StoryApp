package com.example.storyapp.customView

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
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
            ),
            R.drawable.icon_story_app_register
        )
        addEditText(
            editText = usernameEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ), context.getString(R.string.hint_username),
            { editText, context, customTextViewErrorLayout, s ->
                Utils.onValidateUsername(
                    editText, context, customTextViewErrorLayout, s
                )
            }
        )
        addEditText(
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
        addEditText(
            editText = passwordEditText,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ),
            context.getString(R.string.hint_password),
            { editText, context, customTextViewErrorLayout, s ->
                Utils.onValidatePassword(
                    editText, context, customTextViewErrorLayout, s
                )
            },
            InputType.TYPE_TEXT_VARIATION_PASSWORD
        )
        addCustomTextViewErrorLayout()
    }

    fun isValidRegister(): Boolean =
        emailEditText.text.isNotEmpty()
                && passwordEditText.text.isNotEmpty()
                && usernameEditText.text.isNotEmpty()
                && customTextViewErrorLayout.visibility != View.VISIBLE
}
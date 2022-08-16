package com.example.storyapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.storyapp.R

open class CustomViewLayout : LinearLayout {

    val customTextViewErrorLayout = CustomTextViewErrorLayout(context)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    open fun setupLayout() {
        val myParam = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        layoutParams = myParam
        orientation = VERTICAL
    }

    open fun addStoryAppLogo(
        layoutParams: LayoutParams,
        iconDrawable: Int = R.drawable.icon_story_app_login
    ) {
        val storyImageView = ImageView(context)
        layoutParams.setMargins(80, 40, 0, 0)
        storyImageView.layoutParams = layoutParams
        storyImageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                iconDrawable
            ) as Drawable
        )

        addView(storyImageView)
    }

    open fun addCustomTextViewErrorLayout() {
        val myParam = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        myParam.setMargins(80, 40, 0, 0)

        customTextViewErrorLayout.layoutParams = myParam
        customTextViewErrorLayout.visibility = View.INVISIBLE

        addView(customTextViewErrorLayout)
    }

    open fun addEditText(
        editText: EditText,
        layoutParams: LayoutParams,
        hint: String,
        onTextChangedListener: ((
            editText: EditText,
            context: Context,
            customViewErrorLayout: CustomTextViewErrorLayout,
            s: CharSequence
        ) -> Unit)? = null,
        inputType: Int? = null
    ) {
        layoutParams.setMargins(80, 40, 80, 0)
        editText.layoutParams = layoutParams

        editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
        editText.hint = hint
        editText.setPadding(20)
        if (inputType != null) editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        if (onTextChangedListener != null) {
            editText.addTextChangedListener(onEditTextChangedListener(
                editText, context, customTextViewErrorLayout, onTextChangedListener
            ))
        }

        addView(editText)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun onEditTextChangedListener(
        editText: EditText, context: Context, customTextViewError: CustomTextViewErrorLayout,
        onTextChangedListener: (
            editText: EditText,
            context: Context,
            customViewErrorLayout: CustomTextViewErrorLayout,
            s: CharSequence
        ) -> Unit
    ) : TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            )
            {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                onTextChangedListener(editText, context, customTextViewError, s)
            }

            override fun afterTextChanged(s: Editable) {
            }
        }
    }
}
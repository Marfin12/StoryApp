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

    open fun addStoryAppLogo(layoutParams: LayoutParams) {
        val storyImageView = ImageView(context)
        layoutParams.setMargins(80, 40, 0, 0)
        storyImageView.layoutParams = layoutParams
        storyImageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.icon_story_app_login
            ) as Drawable
        )

        addView(storyImageView)
    }

    open fun addCommonEditText(
        editText: EditText,
        layoutParams: LayoutParams,
        hint: String
    ) {
        layoutParams.setMargins(80, 40, 80, 0)
        editText.layoutParams = layoutParams

        editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
        editText.hint = hint

        onCommonFieldChangedListener(editText, context, customTextViewErrorLayout)

        addView(editText)
    }

    open fun addPasswordEditText(
        editText: EditText,
        layoutParams: LayoutParams,
        hint: String
    ) {
        val editText = EditText(context)
        layoutParams.setMargins(80, 40, 80, 0)
        editText.layoutParams = layoutParams

        editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
        editText.hint = hint
        editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        customTextViewErrorLayout.layoutParams = layoutParams
        customTextViewErrorLayout.visibility = View.INVISIBLE

        onPasswordChangedListener(editText, context, customTextViewErrorLayout)

        addView(editText)
        addView(customTextViewErrorLayout)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    open fun onCommonFieldChangedListener(
        editText: EditText, context: Context, customTextViewError: CustomTextViewErrorLayout
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
                if (s.isEmpty()) {
                    editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext_error) as Drawable
                    customTextViewError.textError = context.getString(R.string.error_empty)
                    customTextViewError.visibility = View.VISIBLE
                } else {
                    editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
                    customTextViewError.visibility = View.INVISIBLE
                }


            }

            override fun afterTextChanged(s: Editable) {
            }
        }
    }

    open fun onPasswordChangedListener(
        editText: EditText, context: Context, customTextViewError: CustomTextViewErrorLayout
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
                if (s.length < 6) {
                    editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext_error) as Drawable
                    customTextViewError.textError = context.getString(R.string.error_password)
                    customTextViewError.visibility = View.VISIBLE
                } else {
                    editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
                    customTextViewError.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        }
    }
}
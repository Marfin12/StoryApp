package com.example.storyapp.core.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

open class AbstractCustomEditText : AppCompatEditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    open fun setupParams() {
        val defaultParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        defaultParams.setMargins(80, 40, 80, 0)

        layoutParams = defaultParams
    }

    open fun setupUI() {
        background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
        hint = context.getString(R.string.hint_username)

        setPadding(20, 20, 20, 20)
    }

    open fun setupBehaviour() {
        setOnEditTextChangeListener()
    }

    private fun setOnEditTextChangeListener() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                background =
                    ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    background =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.bg_edittext_error
                        ) as Drawable
                    error = context.getString(R.string.error_empty)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}
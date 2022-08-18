package com.example.storyapp.core.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomEmailEditText : AbstractCustomEditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setupParams()
        setupUI()
        setupBehaviour()
    }

    override fun setupUI() {
        super.setupUI()
        hint = context.getString(R.string.hint_email)
    }

    override fun setupBehaviour() {
        super.setupBehaviour()
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

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isEmpty()) {
                        background =
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.bg_edittext_error
                            ) as Drawable
                        error = context.getString(R.string.error_empty)
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        background =
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.bg_edittext_error
                            ) as Drawable
                        error = context.getString(R.string.error_email)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        }
    }
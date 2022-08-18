package com.example.storyapp.core.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.storyapp.R
import com.example.storyapp.core.EMPTY_STRING

class CustomUserEditText : AbstractCustomEditText {
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

    override fun setupBehaviour() {
        setOnEditTextChangeListener()
    }

    private fun setOnEditTextChangeListener() {
        addTextChangedListener {
            object : TextWatcher {
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
                            ContextCompat.getDrawable(context, R.drawable.bg_edittext_error) as Drawable
                        error = context.getString(R.string.error_empty)
                    }
                    else {
                        background =
                            ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
                        error = EMPTY_STRING
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            }
        }
    }

}
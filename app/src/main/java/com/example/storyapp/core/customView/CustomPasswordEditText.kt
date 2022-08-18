package com.example.storyapp.core.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomPasswordEditText : AbstractCustomEditText, View.OnTouchListener {
    private lateinit var eyeButtonImage: Drawable
    private var isEyeClosed = false

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
        hint = context.getString(R.string.hint_password)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        showEyeOpen()

        setButtonDrawables(endOfTheText = eyeButtonImage)
    }

    override fun setupBehaviour() {
        super.setupBehaviour()

        setOnTouchListener(this)
        setOnEditTextChangeListener()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
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
                when {
                    s.length < 6 -> {
                        background =
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.bg_edittext_error
                            ) as Drawable
                        error = context.getString(R.string.error_password)
                    }
                    else -> {
                        background =
                            ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isEyeButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (eyeButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isEyeButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - eyeButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isEyeButtonClicked = true
                }
            }
            if (isEyeButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (isEyeClosed) {
                            isEyeClosed = false
                            showEyeOpen()
                        } else {
                            isEyeClosed = true
                            showEyeClose()
                        }

                        invalidateDrawables()
                        setSelection(text?.length ?: 0)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }

    private fun showEyeOpen() {
        eyeButtonImage =
            ContextCompat.getDrawable(context, R.drawable.icon_eye_password_open) as Drawable
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun showEyeClose() {
        eyeButtonImage =
            ContextCompat.getDrawable(context, R.drawable.icon_eye_password_close) as Drawable
        inputType = InputType.TYPE_CLASS_TEXT
    }

    private fun invalidateDrawables() {
        setButtonDrawables()
        setButtonDrawables(endOfTheText = eyeButtonImage)
    }

}
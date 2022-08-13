package com.example.storyapp.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomTextViewErrorLayout : LinearLayout {

    var textError = context.getString(R.string.error_empty)

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val myParam = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        orientation = HORIZONTAL
        layoutParams = myParam

        val imageViewParam = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        val textViewParam = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        textViewParam.setMargins(20, 4, 0, 0)

        val imageView = ImageView(context)
        imageView.setImageDrawable(ContextCompat.getDrawable(context,
            R.drawable.icon_password_error
        ) as Drawable)
        imageView.layoutParams = imageViewParam

        val textView = TextView(context)
        textView.setTextColor(resources.getColor(R.color.error_password, context.theme))
        textView.layoutParams = textViewParam
        textView.textSize = 14.0f
        textView.text = textError

        addView(imageView)
        addView(textView)
    }
}
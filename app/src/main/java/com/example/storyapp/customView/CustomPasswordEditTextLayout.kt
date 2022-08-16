package com.example.storyapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomPasswordEditTextLayout : CustomViewLayout {

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
    }

    fun addEyeImageView(imageView: ImageView) {
        val imageViewParam = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageViewParam.setMargins(-180, 48, 0, 0)
        imageView.setImageDrawable(ContextCompat.getDrawable(context,
            R.drawable.icon_eye_password_open
        ) as Drawable)
        imageView.layoutParams = imageViewParam

        addView(imageView)
    }
}
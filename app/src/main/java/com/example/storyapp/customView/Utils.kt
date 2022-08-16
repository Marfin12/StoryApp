package com.example.storyapp.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class Utils {
    companion object {
        fun onValidatePassword(
            editText: EditText,
            context: Context,
            customTextViewError: CustomTextViewErrorLayout,
            s: CharSequence
        ) {
            if (s.length < 6) {
                    editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext_error) as Drawable
                    customTextViewError.textView.text = context.getString(R.string.error_password)
                    customTextViewError.visibility = View.VISIBLE
                } else {
                    editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
                    customTextViewError.visibility = View.INVISIBLE
                }

            onValidateEmptyText(editText, context, customTextViewError, s)
        }

        fun onValidateEmail(
            editText: EditText,
            context: Context,
            customTextViewError: CustomTextViewErrorLayout,
            s: CharSequence
        ) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext_error) as Drawable
                customTextViewError.textView.text = context.getString(R.string.error_email)
                customTextViewError.visibility = View.VISIBLE
            } else {
                editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
                customTextViewError.visibility = View.INVISIBLE
            }

            onValidateEmptyText(editText, context, customTextViewError, s)
        }

        fun onValidateUsername(
            editText: EditText,
            context: Context,
            customTextViewError: CustomTextViewErrorLayout,
            s: CharSequence
        ) {
            editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
            customTextViewError.visibility = View.INVISIBLE

            onValidateEmptyText(editText, context, customTextViewError, s)
        }

        private fun onValidateEmptyText(
            editText: EditText,
            context: Context,
            customTextViewError: CustomTextViewErrorLayout,
            s: CharSequence
        ) {
            if (s.isEmpty() || s == "") {
                editText.background = ContextCompat.getDrawable(context, R.drawable.bg_edittext_error) as Drawable
                customTextViewError.textView.text = context.getString(R.string.error_empty)
                customTextViewError.visibility = View.VISIBLE
            }
        }
    }
}
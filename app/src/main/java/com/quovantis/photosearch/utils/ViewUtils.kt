package com.quovantis.photosearch.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ViewUtils {

    fun showShortToast(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun hideKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (context is Activity) {
            var view = context.currentFocus
            if (view == null) {
                view = View(context)
            }
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

fun EditText.getValueChangeStateFlow(): StateFlow<String?> {
    val query:MutableStateFlow<String?> = MutableStateFlow(null)

    doAfterTextChanged {
        query.value = it.toString()
    }
    return query
}
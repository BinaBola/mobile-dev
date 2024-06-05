package com.binabola.app.presentation

import android.content.Context
import android.widget.Toast

class AppUtil {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
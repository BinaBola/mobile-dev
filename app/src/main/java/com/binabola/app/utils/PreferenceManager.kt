package com.binabola.app.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager (context: Context){
    private var prefs: SharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()


    fun clearAllPreferences() {
        editor.remove(KEY_TOKEN)
        editor.apply()
    }
}
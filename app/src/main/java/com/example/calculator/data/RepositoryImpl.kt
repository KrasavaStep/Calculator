package com.example.calculator.data

import android.content.Context
import javax.inject.Inject

private const val SHARED_PREFERENCES_VALUE_NAME = "sp_value"
private const val KEY_VALUE = "key_value"
private const val EMPTY_VALUE = ""
private const val KEY_THEME = "key_theme"
private const val KEY_LANGUAGE = "key_language"

class RepositoryImpl @Inject constructor(context: Context) : Repository {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_VALUE_NAME, Context.MODE_PRIVATE)

    override fun saveLastValue(value: String) {
        sharedPreferences.edit().putString(KEY_VALUE, value).apply()
    }

    override fun getValue(): String {
        return sharedPreferences.getString(KEY_VALUE, "") ?: ""
    }

    override fun clearValue() {
        sharedPreferences.edit().putString(KEY_VALUE, EMPTY_VALUE).apply()
    }

    override fun getTheme():  Int {
        return sharedPreferences.getInt(KEY_THEME, 0)
    }
    override fun saveTheme(themeCode: Int) {
        sharedPreferences.edit().putInt(KEY_THEME, themeCode).apply()
    }

}
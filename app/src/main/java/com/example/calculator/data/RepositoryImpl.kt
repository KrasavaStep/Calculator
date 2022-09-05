package com.example.calculator.data

import android.content.Context
import javax.inject.Inject

private const val SHARED_PREFERENCES_VALUE_NAME = "sp_value"
private const val KEY_VALUE = "key_value"

class RepositoryImpl @Inject constructor(context: Context) : Repository {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_VALUE_NAME, Context.MODE_PRIVATE)

    override fun saveLastValue(value: String) {
        sharedPreferences.edit().putString(KEY_VALUE, value).apply()
    }

    override fun getValue(): String {
        return sharedPreferences.getString(KEY_VALUE, "") ?: ""
    }
}
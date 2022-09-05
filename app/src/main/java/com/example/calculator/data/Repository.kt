package com.example.calculator.data

import android.content.SharedPreferences

interface Repository {
    fun saveLastValue(value: String)
    fun getValue() : String
}
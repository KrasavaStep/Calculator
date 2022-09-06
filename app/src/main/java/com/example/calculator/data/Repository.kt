package com.example.calculator.data

interface Repository {
    fun saveLastValue(value: String)
    fun getValue() : String
}
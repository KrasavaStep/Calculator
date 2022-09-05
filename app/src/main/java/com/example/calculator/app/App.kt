package com.example.calculator.app

import android.app.Application
import com.example.calculator.di.AppComponent
import com.example.calculator.di.AppModule
import com.example.calculator.di.DaggerAppComponent

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(context = this)).build()
    }
    companion object {
        lateinit var appComponent: AppComponent
    }
}
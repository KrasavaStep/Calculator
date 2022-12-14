package com.example.calculator.di

import android.content.Context
import com.example.calculator.app.ResourceProvider
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideResources(): ResourceProvider {
        return ResourceProvider(context)
    }
}
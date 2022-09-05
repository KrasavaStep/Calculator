package com.example.calculator.di

import android.content.Context
import com.example.calculator.data.Repository
import com.example.calculator.data.RepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideRepository(context: Context) : Repository{
        return RepositoryImpl(context = context)
    }

}
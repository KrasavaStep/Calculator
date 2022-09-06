package com.example.calculator.di

import com.example.calculator.view.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {

    fun inject(fragment : HomeFragment)
}
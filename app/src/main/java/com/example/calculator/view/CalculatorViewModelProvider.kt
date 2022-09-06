package com.example.calculator.view

import android.app.Application
import com.example.calculator.app.ResourceProvider
import com.example.calculator.base.ViewModelFactory
import com.example.calculator.data.Repository
import javax.inject.Inject

class CalculatorViewModelProvider @Inject constructor(
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) :
    ViewModelFactory<CalculatorViewModel>(CalculatorViewModel::class.java) {
    override fun createViewModel(): CalculatorViewModel {
        return CalculatorViewModel(repository, resourceProvider)
    }
}
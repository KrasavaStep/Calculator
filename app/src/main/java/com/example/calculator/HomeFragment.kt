package com.example.calculator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.app.App
import com.example.calculator.databinding.ActivityMainBinding.bind
import com.example.calculator.databinding.FragmentHomeBinding
import com.example.calculator.view.CalculatorViewModel
import com.example.calculator.view.CalculatorViewModelProvider
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelProvider: CalculatorViewModelProvider

    private lateinit var viewModel: CalculatorViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        viewModel.performCalculation("2+2*2")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelProvider)[CalculatorViewModel::class.java]
    }

}
package com.example.calculator.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.R
import com.example.calculator.app.App
import com.example.calculator.databinding.FragmentHomeBinding
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelProvider: CalculatorViewModelProvider

    private lateinit var viewModel: CalculatorViewModel

    private var textChanged = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.apply {
            clearTextBtn.setOnClickListener {
                currentExpression.text = ""
                viewModel.clearSavedValue()
            }
            backspaceBtn.setOnClickListener {
                currentExpression.text =
                    currentExpression.text.replaceFirst(".$".toRegex(), "")
            }
            rightBracket.setOnClickListener { currentExpression.append("(") }
            leftBracket.setOnClickListener { currentExpression.append(")") }
            divide.setOnClickListener {
                viewModel.checkSign(currentExpression.text.toString(), '/')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            multiply.setOnClickListener {
                viewModel.checkSign(currentExpression.text.toString(), '*')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            substraction.setOnClickListener {
                viewModel.checkSign(currentExpression.text.toString(), '-')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            addition.setOnClickListener {
                viewModel.checkSign(currentExpression.text.toString(), '+')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            dotBtn.setOnClickListener {
                viewModel.checkSign(currentExpression.text.toString(), '.')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            equalsBtn.setOnClickListener {
                Log.e("task", "d")
                viewModel.performCalculation(currentExpression.text.toString())
                viewModel.expressionLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = it
                }
            }
            oneBtn.setOnClickListener { currentExpression.append("1") }
            twoBtn.setOnClickListener { currentExpression.append("2") }
            threeBtn.setOnClickListener { currentExpression.append("3") }
            fourBtn.setOnClickListener { currentExpression.append("4") }
            fiveBtn.setOnClickListener { currentExpression.append("5") }
            sixBtn.setOnClickListener { currentExpression.append("6") }
            sevenBtn.setOnClickListener { currentExpression.append("7") }
            eightBtn.setOnClickListener { currentExpression.append("8") }
            nineBtn.setOnClickListener { currentExpression.append("9") }
            zeroBtn.setOnClickListener { currentExpression.append("0") }
        }

        binding.currentExpression.doOnTextChanged { text, start, before, count ->
            if (textChanged != text) {
                textChanged = text.toString()
                viewModel.saveValue(textChanged)
            }
        }

        viewModel.getSavedValue()
        viewModel.savedValueLiveData.observe(viewLifecycleOwner) {
            binding.currentExpression.text = it
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelProvider)[CalculatorViewModel::class.java]
    }

}
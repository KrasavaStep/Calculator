package com.example.calculator.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.R
import com.example.calculator.app.App
import javax.inject.Inject
import com.example.calculator.app.DIVISION_BY_ZERO_KEY
import com.example.calculator.app.ERROR_KEY
import com.example.calculator.databinding.FragmentHomeBinding

private const val TEXT_KEY = "saved_text"
private const val DIALOG_KEY = "is_showing_dialog"

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelProvider: CalculatorViewModelProvider

    private lateinit var viewModel: CalculatorViewModel

    private var textChanged = ""

    private var isShowingDialog = false

    private var themeDialog: AlertDialog? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_KEY, binding.currentExpression.text.toString())
        outState.putBoolean(DIALOG_KEY, isShowingDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.bind(view)

        val savedTextValue = savedInstanceState?.getString(TEXT_KEY)

        if (savedTextValue != null) {
            binding.currentExpression.text = savedTextValue
            isShowingDialog = savedInstanceState.getBoolean(DIALOG_KEY, false);
            if (isShowingDialog) {
                createThemeDialog()
            }
        }

        viewModel.getThemeCode()
        viewModel.themeLiveData.observe(viewLifecycleOwner) {
            if (it == 0)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.apply {
            clearTextBtn.setOnClickListener {
                currentExpression.text = ""
                viewModel.clearSavedValue()
            }
            backspaceBtn.setOnClickListener {
                if (currentExpression.text == DIVISION_BY_ZERO_KEY ||
                    currentExpression.text == ERROR_KEY
                )
                    currentExpression.text = ""
                else
                    currentExpression.text =
                        currentExpression.text.replaceFirst(".$".toRegex(), "")
            }
            rightBracket.setOnClickListener {
                checkTextField()
                currentExpression.append("(")
            }
            leftBracket.setOnClickListener {
                checkTextField()
                currentExpression.append(")")
            }
            divide.setOnClickListener {
                checkTextField()
                viewModel.checkSign(currentExpression.text.toString(), '/')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            multiply.setOnClickListener {
                checkTextField()
                viewModel.checkSign(currentExpression.text.toString(), '*')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            substraction.setOnClickListener {
                checkTextField()
                viewModel.checkSign(currentExpression.text.toString(), '-')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            addition.setOnClickListener {
                checkTextField()
                viewModel.checkSign(currentExpression.text.toString(), '+')
                viewModel.signLiveData.observe(viewLifecycleOwner) {
                    currentExpression.text = ""
                    currentExpression.append(it)
                }
            }
            dotBtn.setOnClickListener {
                checkTextField()
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
            oneBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("1")
            }
            twoBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("2")
            }
            threeBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("3")
            }
            fourBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("4")
            }
            fiveBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("5")
            }
            sixBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("6")
            }
            sevenBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("7")
            }
            eightBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("8")
            }
            nineBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("9")
            }
            zeroBtn.setOnClickListener {
                checkTextField()
                currentExpression.append("0")
            }
        }

        binding.currentExpression.doOnTextChanged { text, start, before, count ->
            if (textChanged != text) {
                textChanged = text.toString()
                viewModel.saveValue(textChanged)
            }
        }

        binding.themeBtn.setOnClickListener {
            createThemeDialog()
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

    private fun checkTextField() {
        binding.apply {
            if (currentExpression.text == DIVISION_BY_ZERO_KEY ||
                currentExpression.text == ERROR_KEY
            )
                currentExpression.text = ""
        }
    }

    override fun onPause() {
        super.onPause()
        if (themeDialog != null && themeDialog?.isShowing!!) {
            themeDialog?.dismiss()
        }
    }

    private fun createThemeDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.actionbar_menu1)
        val items = arrayOf(getString(R.string.light_mode), getString(R.string.night_mode))
        viewModel.getThemeCode()
        viewModel.themeLiveData.observe(viewLifecycleOwner) {
            var checkedItem = it
            builder.setSingleChoiceItems(items, checkedItem) { dialog, which ->
                when (which) {
                    0 -> checkedItem = 0
                    1 -> checkedItem = 1
                }
            }
            builder.setNegativeButton(getText(R.string.negative_btn)) { dialog, i ->
                dialog.dismiss()
                isShowingDialog = false
            }
            builder.setPositiveButton(getText(R.string.positive_btn)) { dialog, i ->
                dialog.dismiss()
                isShowingDialog = false
                viewModel.saveTheme(checkedItem)
                if (checkedItem == 0)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            themeDialog = builder.create()
            themeDialog?.setCanceledOnTouchOutside(false)
            themeDialog?.show()
            isShowingDialog = true
        }
    }
}


package com.example.calculator.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculator.R
import com.example.calculator.app.ResourceProvider
import com.example.calculator.data.Repository
import java.util.*

private const val DIVISION_BY_ZERO_KEY = "dbz"
private const val ERROR_KEY = "error"
private const val EMPTY_KEY = "empty"
private const val ONLY_DIGIT = "only_digit"

class CalculatorViewModel(
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _expressionLiveData = MutableLiveData<String>()
    val expressionLiveData: LiveData<String> = _expressionLiveData

    private val _savedValueLiveData = MutableLiveData<String>()
    val savedValueLiveData: LiveData<String> = _savedValueLiveData

    private val _signLiveData = MutableLiveData<String>()
    val signLiveData: LiveData<String> = _signLiveData

    fun performCalculation(expression: String) {
        var finalAnswer = ""

        val preparedExpression = prepareExpression(expression)
        finalAnswer = when (preparedExpression) {
            DIVISION_BY_ZERO_KEY -> resourceProvider.getString(R.string.divide_by_zero)
            ERROR_KEY -> resourceProvider.getString(R.string.error)
            EMPTY_KEY -> expression
            ONLY_DIGIT -> expression
            else -> {
                val rpn = expressionToRPN(preparedExpression)
                val answer = rpnToAnswer(rpn)
                checkAnswer(answer)
            }
        }
        _expressionLiveData.value = finalAnswer
        repository.saveLastValue(finalAnswer)
    }

    private fun checkAnswer(answer: Double): String {
        val fractionalPart = answer.toString().substringAfter('.')
        return if (fractionalPart.length == 1 && fractionalPart.toInt() == 0)
            answer.toString().substringBefore('.')
        else answer.toString()
    }

    private fun prepareExpression(expression: String): String {
        var preparedExpression = ""
        var index = 0

        if (expression.isEmpty()) return EMPTY_KEY

        if (!expression.contains(Regex("""[+\-*/().]"""))) {
            return ONLY_DIGIT
        }

        if (expression.substringAfter('/').isNotEmpty() &&
            expression.substringAfter('/')[0] == '0'
        ) {
            return DIVISION_BY_ZERO_KEY
        }
        if ((expression.substringBefore('(').last() == '*' ||
                    expression.substringBefore('(')
                        .last() == '/') && expression.substringBefore('(').isNotEmpty()
        ) {
            return ERROR_KEY
        }
        if ((expression.substringAfter('(').first() == '*' ||
                    expression.substringAfter('(')
                        .first() == '/') && expression.substringAfter('(').isNotEmpty()
        ) {
            return ERROR_KEY
        }
        if (expression.substringBefore(')').isNotEmpty()
            && getPriority(expression.substringBefore(')').last()) > 1
        ) return ERROR_KEY

        if (expression.substringAfter(')').isNotEmpty() &&
            getPriority(expression.substringAfter(')').first()) > 1
        ) return ERROR_KEY

        while (index < expression.length) {
            if (expression[index] == '-' || expression[index] == '+') {
                if (index == 0)
                    preparedExpression += '0'
                else if (expression[index - 1] == '(')
                    preparedExpression += '0'
            }
            if (expression[index] == '*' || expression[index] == '/') {

            }
            preparedExpression += expression[index]
            index++
        }
        return preparedExpression
    }

    private fun expressionToRPN(expression: String): String {
        var current = ""
        val stack: Stack<Char> = Stack<Char>()

        var priority = 0
        for ((i, character) in expression.withIndex()) {
            priority = getPriority(character)

            if (priority == 0) current += character
            if (priority == 1) stack.push(character)

            if (priority > 1) {
                current += ' '

                while (!stack.empty()) {
                    if (getPriority(stack.peek()) >= priority)
                        current += stack.pop()
                    else break
                }
                stack.push(character)
            }

            if (priority == -1) {
                current += ' '
                while (getPriority(stack.peek()) != 1)
                    current += stack.pop()
                stack.pop()
            }
        }

        while (!stack.empty())
            current += stack.pop()


        return current
    }

    private fun rpnToAnswer(rpn: String): Double {
        var operand = ""
        val stack: Stack<Double> = Stack<Double>()

        var index = 0
        while (index < rpn.length) {
            if (rpn[index] == ' ') {
                index++
                continue
            }

            if (getPriority(rpn[index]) == 0) {
                while (rpn[index] != ' ' && getPriority(rpn[index]) == 0) {
                    operand += rpn[index++]
                    if (index == rpn.length) break
                }
                stack.push(operand.toDouble())
                operand = ""
            }

            if (getPriority(rpn[index]) > 1) {
                val a = stack.pop()
                val b = stack.pop()

                if (rpn[index] == '+') stack.push(b + a)
                if (rpn[index] == '-') stack.push(b - a)
                if (rpn[index] == '*') stack.push(b * a)
                if (rpn[index] == '/') stack.push(b / a)
            }
            index++
        }

        return stack.pop()
    }

    private fun getPriority(token: Char): Int {
        return if (token == '*' || token == '/') 3
        else if (token == '+' || token == '-') 2
        else if (token == '(') 1
        else if (token == ')') -1
        else 0
    }

    fun getSavedValue() {
        _savedValueLiveData.value = repository.getValue()
    }

    fun clearSavedValue() {
        repository.clearValue()
    }

    fun saveValue(text: String){
        repository.saveLastValue(text)
    }

    fun checkSign(expression: String, sign: Char) {
        when (sign) {
            '-' -> {
                _signLiveData.value = placeSign('-', expression)
            }
            '+' -> {
                _signLiveData.value = placeSign('+', expression)
            }
            '*' -> {
                _signLiveData.value = placeSign('*', expression)
            }
            '/' -> {
                _signLiveData.value = placeSign('/', expression)
            }
            '.' -> {
                _signLiveData.value = placeSign('.', expression)
            }
        }
    }

    private fun placeSign(sign: Char, expression: String): String {
        var expr = expression
        if (expr.isEmpty()) {
            if (sign == '.')
                return "0."
            expr += sign
            return expr
        }
        if (sign == '.') {
            if (expr.last() == '.') {
                return expr.replace(expr.last(), sign)
            }
            if (getPriority(expr.last()) == 0) {
                if (expr.length == 1) {
                    expr += '.'
                    return expr
                }

                if (expr.length > 1) {
                    val expressionList = expr.split("+", "-", "/", "*")
                    val listItem = expressionList.last()
                    if (listItem.isEmpty()) {
                        expr += "0."
                        return expr
                    } else if (listItem.contains('.')) {
                        return expr
                    }
                }
            }
            if (getPriority(expr.last()) >= 1) {
                expr += "0."
                return expr
            }
            if (getPriority(expr.last()) == -1) {
                expr += "*0."
                return expr
            }
        }
        if (getPriority(expr.last()) > 1) {
            if (expr.last() == '-' && sign == '-') {
                expr += "(-"
                return expr
            }
            if (getPriority(expr.last()) > 1) {
                return expr.replace(".$".toRegex(), sign.toString())
            }
        } else {
            expr += sign.toString()
            return expr
        }
        return expr
    }
}
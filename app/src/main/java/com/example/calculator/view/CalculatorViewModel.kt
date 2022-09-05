package com.example.calculator.view

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.calculator.data.Repository
import java.util.*

class CalculatorViewModel(private val repository: Repository) : ViewModel() {

    fun performCalculation(expression: String) {
        val rpn = expressionToRPN(expression)
        val answer = RPNToAnswer(rpn)
        Log.e("task", "$rpn === $answer")
    }

    private fun expressionToRPN(expression: String): String {
        var current = ""
        val stack: Stack<Char> = Stack<Char>()

        var priority = 0
        for (character in expression) {
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

    private fun RPNToAnswer(rpn: String): Double {
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

}
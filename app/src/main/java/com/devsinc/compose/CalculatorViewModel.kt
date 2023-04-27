package com.devsinc.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class CalculatorViewModel: ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDelete()
        }
    }

    private fun performDelete() {
        when {
            state.secondNumber.isNotBlank() -> state = state.copy(secondNumber = state.secondNumber.dropLast(1))
            state.operation != null -> state = state.copy(operation = null)
            state.firstNumber.isNotBlank() -> state = state.copy(firstNumber = state.firstNumber.dropLast(1))
        }
    }

    private fun performCalculation() {
        if(state.operation != null && state.secondNumber.isNotBlank()) {
            val firstNumber = state.firstNumber.toDouble()
            val secondNumber = state.secondNumber.toDouble()
            val result = when(state.operation) {
                CalculatorOperation.Add -> firstNumber + secondNumber
                CalculatorOperation.Subtract -> firstNumber - secondNumber
                CalculatorOperation.Multiply -> firstNumber * secondNumber
                CalculatorOperation.Divide -> firstNumber / secondNumber
                null -> return
            }
            state = state.copy(firstNumber = ((result * 100.0).roundToInt() / 100.0).toString(), secondNumber = "", operation = null)
        }
    }

    private fun enterOperation(operation: Any) {
        if(state.firstNumber.isNotBlank()) {
            state = state.copy(operation = operation as CalculatorOperation)
        }
    }

    private fun enterDecimal() {
        if(state.operation == null) {
            if(!state.firstNumber.contains(".")) {
                state = state.copy(firstNumber = state.firstNumber + ".")
            }
        } else {
            if(!state.secondNumber.contains(".")) {
                state = state.copy(secondNumber = state.secondNumber + ".")
            }
        }
    }

    private fun enterNumber(number: Int) {
        state = if(state.operation == null) {
            if(state.firstNumber == "0") {
                state.copy(firstNumber = number.toString())
            } else {
                state.copy(firstNumber = state.firstNumber + number)
            }
        } else {
            if(state.secondNumber == "0") {
                state.copy(secondNumber = number.toString())
            } else {
                state.copy(secondNumber = state.secondNumber + number)
            }
        }
    }
}

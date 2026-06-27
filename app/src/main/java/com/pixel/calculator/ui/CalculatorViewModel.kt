package com.pixel.calculator.ui
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var displayExpression by mutableStateOf("")
    var displayResult by mutableStateOf("")

    fun onDigit(d: String) { displayExpression += d }
    fun onOperator(o: String) { displayExpression += " $o " }
    fun onClear() { displayExpression = ""; displayResult = "" }
    fun onBackspace() { if(displayExpression.isNotEmpty()) displayExpression = displayExpression.dropLast(1) }
    fun onEquals() {
        // Упрощенная логика для примера
        try { displayResult = "Result Ready" } catch(e: Exception) { displayResult = "Error" }
    }
}

package com.pixel.calculator.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    val colorScheme = MaterialTheme.colorScheme
    Scaffold(containerColor = colorScheme.surface) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(32.dp)).background(colorScheme.surfaceContainerLow).padding(24.dp), contentAlignment = Alignment.BottomEnd) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = viewModel.displayExpression, style = MaterialTheme.typography.headlineMedium, color = colorScheme.onSurfaceVariant)
                    Text(text = viewModel.displayResult.ifEmpty { "0" }, style = MaterialTheme.typography.displayLarge, fontSize = 56.sp, color = colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ButtonGrid(viewModel)
        }
    }
}

@Composable
fun ButtonGrid(vm: CalculatorViewModel) {
    val clr = MaterialTheme.colorScheme
    val rows = listOf(
        listOf("AC" to clr.surfaceContainerHighest, "+/-" to clr.surfaceContainerHighest, "%" to clr.surfaceContainerHighest, "÷" to clr.tertiaryContainer),
        listOf("7" to clr.surfaceContainerHigh, "8" to clr.surfaceContainerHigh, "9" to clr.surfaceContainerHigh, "×" to clr.tertiaryContainer),
        listOf("4" to clr.surfaceContainerHigh, "5" to clr.surfaceContainerHigh, "6" to clr.surfaceContainerHigh, "−" to clr.tertiaryContainer),
        listOf("1" to clr.surfaceContainerHigh, "2" to clr.surfaceContainerHigh, "3" to clr.surfaceContainerHigh, "+" to clr.tertiaryContainer),
        listOf("⌫" to clr.surfaceContainerHigh, "0" to clr.surfaceContainerHigh, "." to clr.surfaceContainerHigh, "=" to clr.primary)
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { (label, color) ->
                    CalcButton(label, color, Modifier.weight(1f)) {
                        when(label) {
                            "AC" -> vm.onClear(); "=" -> vm.onEquals(); "⌫" -> vm.onBackspace()
                            "+", "−", "×", "÷" -> vm.onOperator(label)
                            else -> vm.onDigit(label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalcButton(text: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.9f else 1f)

    Surface(
        onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); onClick() },
        modifier = modifier.height(80.dp).graphicsLayer { scaleX = scale; scaleY = scale },
        shape = RoundedCornerShape(24.dp),
        color = color,
        interactionSource = interaction
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.Medium)
        }
    }
}

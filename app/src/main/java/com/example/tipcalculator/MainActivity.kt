package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var orderAmount by remember { mutableStateOf("") }
    var dishesCount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(10f) }
    var selectedDiscount by remember { mutableIntStateOf(3) }

    var tipAmount by remember { mutableStateOf("") }
    var discountAmount by remember { mutableStateOf("") }
    var totalAmount by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Калькулятор чаевых",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Сумма заказа
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Сумма заказа:", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = orderAmount,
                onValueChange = { orderAmount = it },
                modifier = Modifier.width(150.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFC0CB),
                    unfocusedContainerColor = Color(0xFFFFC0CB),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Количество блюд
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Количество блюд:", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = dishesCount,
                onValueChange = { dishesCount = it },
                modifier = Modifier.width(80.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFC0CB),
                    unfocusedContainerColor = Color(0xFFFFC0CB),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Слайдер чаевых
        Text("Чаевые:", fontSize = 14.sp)
        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..25f,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0", fontSize = 14.sp)
            Text("25", fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Радиокнопки скидки
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Скидка:", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(selected = selectedDiscount == 3, onClick = null)
                Text("3%", fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(selected = selectedDiscount == 5, onClick = null)
                Text("5%", fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(selected = selectedDiscount == 7, onClick = null)
                Text("7%", fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(selected = selectedDiscount == 10, onClick = null)
                Text("10%", fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка расчета
        Button(
            onClick = {
                val amount = orderAmount.toDoubleOrNull()
                val dishes = dishesCount.toIntOrNull()

                if (amount != null && dishes != null && dishes > 0) {
                    // 1. Выбор скидки программно
                    selectedDiscount = when {
                        dishes <= 2 -> 3
                        dishes in 3..5 -> 5
                        dishes in 6..10 -> 7
                        else -> 10
                    }

                    // 2. Расчет
                    val tip = amount * tipPercent / 100.0
                    val discount = amount * selectedDiscount / 100.0
                    val total = amount + tip - discount

                    // 3. Вывод (String.format с Locale.US, чтобы были точки, а не запятые)
                    tipAmount = String.format(Locale.US, "%.2f", tip)
                    discountAmount = String.format(Locale.US, "%.2f", discount)
                    totalAmount = String.format(Locale.US, "%.2f", total)
                } else {
                    totalAmount = "Ошибка ввода"
                    tipAmount = "-"
                    discountAmount = "-"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Вывод результата
        Text("Чаевые: $tipAmount", fontSize = 16.sp)
        Text("Скидка: $discountAmount", fontSize = 16.sp)
        Text("Итого: $totalAmount", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TipCalculatorTheme {
        MainScreen()
    }
}
package com.example.finance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddCategoryBlock(
    name: String,
    colorHex: String,
    onNameChange: (String) -> Unit,
    onColorChange: (String) -> Unit
) {
    val colors = listOf(
        "#EF5350", "#E53935", "#B71C1C",
        "#EC407A", "#F06292", "#AD1457",
        "#AB47BC", "#7B1FA2", "#CE93D8",
        "#42A5F5", "#1565C0", "#82B1FF",
        "#26C6DA", "#00838F", "#80DEEA",
        "#66BB6A", "#2E7D32", "#A5D6A7",
        "#FFA726", "#FF6F00", "#FFD54F",
        "#8D6E63", "#546E7A"
    )
    Column {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Новая категория") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("Цвет категории")
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            colors.chunked(8).forEach { rowColors ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    rowColors.forEach { hex ->
                        val color = try {
                            Color(android.graphics.Color.parseColor(hex))
                        } catch (e: Exception) {
                            Color.Gray
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (hex == colorHex) 3.dp else 0.dp,
                                    color = Color.Black,
                                    shape = CircleShape
                                )
                                .clickable { onColorChange(hex) }
                        )
                    }
                }
            }
        }
    }
}

package com.example.finance.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finance.ui.model.TransactionType
import com.example.finance.ui.model.TransactionUiModel


private fun colorFromHex(hex: String?): Color {
    return try {
        if (hex.isNullOrBlank()) Color.Gray else
            Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        Color.Gray
    }
}

@Composable
fun TransactionCard(item: TransactionUiModel) {
    val amountColor = when (item.type) {
        TransactionType.INCOME -> Color(0xFF2E7D32)
        TransactionType.EXPENSE -> Color(0xFFC62828)
        else -> {}
    }
    val categoryColor = colorFromHex(item.categoryColorHex)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(categoryColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.categoryName,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    text = if (item.type == TransactionType.INCOME)
                        "+ ${item.amount}"
                    else
                        "- ${item.amount}",
                    style = MaterialTheme.typography.titleMedium,
                    color = amountColor as Color
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = item.transactionDate, color = Color.Gray)
            if (!item.comment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.comment)
            }
        }
    }
}
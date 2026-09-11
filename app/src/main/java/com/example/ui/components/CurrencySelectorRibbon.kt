package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Currency
import com.example.ui.theme.Primary
import com.example.ui.theme.SurfaceContainerHigh

@Composable
fun CurrencySelectorRibbon(
  selectedCurrency: Currency,
  onCurrencySelected: (Currency) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier =
      modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(32.dp))
        .background(SurfaceContainerHigh)
        .padding(4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Currency.entries.forEach { curr ->
      val isSelected = curr == selectedCurrency
      Box(
        modifier =
          Modifier
            .weight(1f)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) Primary else Color.Transparent)
            .clickable { onCurrencySelected(curr) }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = "${curr.code} (${curr.symbol})",
          style =
            MaterialTheme.typography.labelMedium.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              fontSize = 12.sp,
            ),
          color =
            if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.ui.TripMateUiState
import com.example.ui.TripMateViewModel
import com.example.ui.components.CurrencySelectorRibbon
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.StarGold
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed

@Composable
fun BudgetScreen(
  uiState: TripMateUiState,
  viewModel: TripMateViewModel,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // 1. Header
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
          text = "Travel Expense & Budget Tracker",
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = "Real-time rupee analytics across transit, stay, food & sightseeing",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // 2. Currency Ribbon
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        CurrencySelectorRibbon(
          selectedCurrency = uiState.currency,
          onCurrencySelected = { viewModel.selectCurrency(it) },
        )
      }
    }

    // 3. Main Budget Overview Card
    item {
      Card(
        modifier =
          Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column {
              Text(
                text = "Total Active Budget",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
              )
              Text(
                text = uiState.currency.format(uiState.baseTotalBudget),
                style =
                  MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                  ),
                color = MaterialTheme.colorScheme.onSurface,
              )
            }

            Box(
              modifier =
                Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(TertiaryFixed)
                  .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = "${uiState.currency.format(uiState.remainingBudget)} Left",
                style =
                  MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00513B),
                  ),
              )
            }
          }

          LinearProgressIndicator(
            progress = { (uiState.totalSpent.toFloat() / uiState.baseTotalBudget.toFloat()) },
            modifier =
              Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = Primary,
            trackColor = SurfaceContainerHigh,
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "Spent: ${uiState.currency.format(uiState.totalSpent)} (${uiState.spentPercentage}%)",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
              color = Primary,
            )
            Text(
              text = "Target: 6 Days Royal Tour",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.outline,
            )
          }
        }
      }
    }

    // 4. Detailed Category Breakdown
    item {
      Column(
        modifier =
          Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Text(
          text = "Category Breakdown",
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )

        val categories =
          listOf(
            Triple("Lodging (Haveli & Hotels)", uiState.lodgingCost, Icons.Default.Hotel),
            Triple("Transit (Vande Bharat & Local)", uiState.transitCost, Icons.Default.Train),
            Triple("Food & Regional Dining", uiState.foodCost, Icons.Default.Restaurant),
            Triple("Activities & Fort Passes", uiState.activitiesCost, Icons.Default.ConfirmationNumber),
          )

        categories.forEach { (catTitle, cost, icon) ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth(),
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                Box(
                  modifier =
                    Modifier
                      .size(38.dp)
                      .clip(CircleShape)
                      .background(SurfaceContainer),
                  contentAlignment = Alignment.Center,
                ) {
                  Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(18.dp),
                  )
                }
                Text(
                  text = catTitle,
                  style =
                    MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.SemiBold,
                    ),
                  color = MaterialTheme.colorScheme.onSurface,
                )
              }

              Text(
                text = uiState.currency.format(cost),
                style =
                  MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                  ),
                color = Primary,
              )
            }
          }
        }
      }
    }

    // 5. Smart Indian Travel Budget Tips
    item {
      Column(
        modifier =
          Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = StarGold,
            modifier = Modifier.size(18.dp),
          )
          Text(
            text = "Smart Savings Tips for India Travel",
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
        }

        val tips =
          listOf(
            "Vande Bharat Express: Book 30 days ahead for CC class to save 40% vs last-minute flights.",
            "Composite Fort Pass: Purchase at Amber Fort ticket counter to access Hawa Mahal, Jantar Mantar, and Nahargarh for ₹400.",
            "Metro & Auto Fare: Always prefer pre-paid booth or ride-hailing apps in Delhi/Jaipur for transparent rates.",
            "Local Thali Dining: Traditional heritage messes (bhojanalayas) offer unlimited refills for ₹250–₹400.",
          )

        tips.forEach { tip ->
          Box(
            modifier =
              Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceContainerLow)
                .padding(12.dp)
          ) {
            Text(
              text = tip,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    }
  }
}

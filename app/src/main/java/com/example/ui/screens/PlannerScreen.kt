package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Currency
import com.example.data.ItineraryActivity
import com.example.data.ItineraryDay
import com.example.data.TripMateRepository
import com.example.ui.TripMateUiState
import com.example.ui.TripMateViewModel
import com.example.ui.components.CurrencySelectorRibbon
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.StarGold
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed

@Composable
fun PlannerScreen(
  uiState: TripMateUiState,
  viewModel: TripMateViewModel,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      // 1. Trip Header Hero Summary
      item {
        PlannerHeroSummary(
          currency = uiState.currency,
          targetAmount = uiState.baseTotalBudget,
        )
      }

      // 2. Currency Selector Ribbon
      item {
        Column(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          Text(
            text = "SELECT DISPLAY CURRENCY",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
              ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          CurrencySelectorRibbon(
            selectedCurrency = uiState.currency,
            onCurrencySelected = { viewModel.selectCurrency(it) },
          )
        }
      }

      // 3. Live Budget Calculator Status Card ("Budget Health")
      item {
        BudgetHealthCard(
          uiState = uiState,
          currency = uiState.currency,
        )
      }

      // 4. AI Optimize Budget Action Banner
      item {
        AiBudgetOptimizerBanner(
          isOptimized = uiState.isAiOptimized,
          onToggleOptimization = { viewModel.toggleAiOptimization() },
        )
      }

      // 5. Trip Schedule Header
      item {
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column {
            Text(
              text = "Trip Schedule",
              style =
                MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                ),
              color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
              text = "2 of 6 Days Planned • Jaipur & Amer",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          Text(
            text = "+ Add Day",
            style =
              MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Primary,
              ),
          )
        }
      }

      // 6. Day-by-Day Timeline Items
      items(uiState.itineraryDays) { day ->
        ItineraryDayCard(
          day = day,
          currency = uiState.currency,
        )
      }

      // 7. Planner Action Buttons
      item {
        Column(
          modifier =
            Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Button(
            onClick = { viewModel.openAddActivityDialog() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = OnPrimary,
              ),
            contentPadding = PaddingValues(vertical = 14.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Add Activity / Expense",
              style =
                MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                ),
            )
          }

          OutlinedButton(
            onClick = { viewModel.downloadPdf() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = null,
              tint = Primary,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Download Itinerary (PDF)",
              style =
                MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = Primary,
                ),
            )
          }
        }
      }
    }

    // Add Activity Dialog
    if (uiState.showAddActivityDialog) {
      AddActivityDialog(
        onDismiss = { viewModel.closeAddActivityDialog() },
        onConfirm = { dayNum, title, time, cost, desc ->
          viewModel.addActivity(dayNum, title, time, cost, desc)
        },
      )
    }
  }
}

@Composable
private fun PlannerHeroSummary(
  currency: Currency,
  targetAmount: Int,
) {
  Box(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(210.dp)
        .clip(RoundedCornerShape(20.dp))
  ) {
    AsyncImage(
      model = TripMateRepository.PLANNER_HERO_JAIPUR_URL,
      contentDescription = "Amber Fort Jaipur",
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )

    // Dark scrim gradient
    Box(
      modifier =
        Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors =
                listOf(
                  Color.Black.copy(alpha = 0.45f),
                  Color.Black.copy(alpha = 0.85f),
                )
            )
          )
    )

    // Details overlay
    Column(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      // Top status pill
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          modifier =
            Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(Color.White.copy(alpha = 0.92f))
              .padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          Icon(
            imageVector = Icons.Default.Sync,
            contentDescription = null,
            tint = Tertiary,
            modifier = Modifier.size(14.dp),
          )
          Text(
            text = "Synced Itinerary",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
              ),
            color = Color(0xFF131B2E),
          )
        }

        Text(
          text = "Nov 12 – Nov 17 • 6 Days",
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.SemiBold,
              color = Color.White.copy(alpha = 0.95f),
            ),
        )
      }

      // Title & stats
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = "Jaipur & Udaipur Royal Tour",
          style =
            MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp,
            ),
          color = Color.White,
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          // Travelers Avatars
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-6).dp),
          ) {
            Box(
              modifier =
                Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(PrimaryFixed)
                  .border(1.5.dp, Color.White, CircleShape),
              contentAlignment = Alignment.Center,
            ) {
              Text(
                text = "A",
                style =
                  MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001F28),
                  ),
              )
            }
            Box(
              modifier =
                Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(SecondaryContainer)
                  .border(1.5.dp, Color.White, CircleShape),
              contentAlignment = Alignment.Center,
            ) {
              Text(
                text = "R",
                style =
                  MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                  ),
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "2 Travelers",
              style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
            )
          }

          Text(
            text = "Total Target: ${currency.format(targetAmount)}",
            style =
              MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryFixed,
              ),
          )
        }
      }
    }
  }
}

@Composable
private fun BudgetHealthCard(
  uiState: TripMateUiState,
  currency: Currency,
) {
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
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = "Budget Health",
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text =
              "${currency.format(uiState.totalSpent)} of ${currency.format(uiState.baseTotalBudget)} planned",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Box(
          modifier =
            Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(TertiaryFixed)
              .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${currency.format(uiState.remainingBudget)} Available",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00513B),
              ),
          )
        }
      }

      // Linear Progress
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
            text = "${uiState.spentPercentage}% Spent",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = Primary,
                fontWeight = FontWeight.Bold,
              ),
          )
          Text(
            text = "Under budget by ${uiState.savingsPercentage}%",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = Tertiary,
                fontWeight = FontWeight.Medium,
              ),
          )
        }
      }

      // Bento 4 Category Breakdown
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        BentoCategory(
          label = "Lodging",
          pct = "44%",
          amount = currency.format(uiState.lodgingCost),
          icon = Icons.Default.Hotel,
          iconColor = Primary,
          modifier = Modifier.weight(1f),
        )
        BentoCategory(
          label = "Transit",
          pct = if (uiState.isAiOptimized) "21%" else "26%",
          amount = currency.format(uiState.transitCost),
          icon = Icons.Default.Train,
          iconColor = Secondary,
          modifier = Modifier.weight(1f),
        )
        BentoCategory(
          label = "Food",
          pct = "17%",
          amount = currency.format(uiState.foodCost),
          icon = Icons.Default.Fastfood,
          iconColor = StarGold,
          modifier = Modifier.weight(1f),
        )
        BentoCategory(
          label = "Activities",
          pct = "13%",
          amount = currency.format(uiState.activitiesCost),
          icon = Icons.Default.LocalActivity,
          iconColor = Tertiary,
          modifier = Modifier.weight(1f),
        )
      }
    }
  }
}

@Composable
private fun BentoCategory(
  label: String,
  pct: String,
  amount: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: Color,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier =
      modifier
        .clip(RoundedCornerShape(12.dp))
        .background(SurfaceContainerLow)
        .padding(8.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconColor,
          modifier = Modifier.size(13.dp),
        )
        Text(
          text = pct,
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
      }
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = MaterialTheme.colorScheme.outline,
      )
      Text(
        text = amount,
        style =
          MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
          ),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
private fun AiBudgetOptimizerBanner(
  isOptimized: Boolean,
  onToggleOptimization: () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors =
      CardDefaults.cardColors(
        containerColor =
          if (isOptimized) TertiaryFixed.copy(alpha = 0.5f) else SurfaceContainerLow
      ),
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Box(
          modifier =
            Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (isOptimized) Tertiary else SecondaryContainer),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = if (isOptimized) Icons.Default.Check else Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp),
          )
        }
        Column {
          Text(
            text = if (isOptimized) "AI Optimization Active!" else "AI Budget Optimizer",
            style =
              MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text =
              if (isOptimized) "Saved ₹3,500 by booking Vande Bharat train & fort combo passes."
              else "Optimize with Vande Bharat train & combo fort passes — Save ~₹3,500",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Button(
        onClick = onToggleOptimization,
        shape = RoundedCornerShape(10.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = if (isOptimized) Tertiary else Primary,
            contentColor = Color.White,
          ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
      ) {
        Text(
          text = if (isOptimized) "Applied" else "Optimize",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        )
      }
    }
  }
}

@Composable
private fun ItineraryDayCard(
  day: ItineraryDay,
  currency: Currency,
) {
  Card(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // Day Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = day.dateString,
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
              ),
            color = Primary,
          )
          Text(
            text = "${day.title} • ${day.locationArea}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      // Activities Timeline
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        day.activities.forEach { act ->
          ActivityTimelineRow(activity = act, currency = currency)
        }
      }
    }
  }
}

@Composable
private fun ActivityTimelineRow(
  activity: ItineraryActivity,
  currency: Currency,
) {
  Row(
    modifier =
      Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(SurfaceContainerLow)
        .padding(10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.Top,
  ) {
    Row(
      modifier = Modifier.weight(1f),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.Top,
    ) {
      // Time pill
      Box(
        modifier =
          Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceContainer)
            .padding(horizontal = 6.dp, vertical = 4.dp)
      ) {
        Text(
          text = activity.time,
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
            ),
          color = Primary,
        )
      }

      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
          text = activity.title,
          style =
            MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = activity.description,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // Photo thumbnail if available
        if (activity.imageUrl != null) {
          AsyncImage(
            model = activity.imageUrl,
            contentDescription = activity.title,
            modifier =
              Modifier
                .size(width = 120.dp, height = 70.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
          )
        }
      }
    }

    // Cost tag
    Box(
      modifier =
        Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(
            if (activity.isFree) TertiaryFixed
            else if (activity.isReserved) SecondaryFixed
            else SurfaceContainer
          )
          .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
      Text(
        text =
          if (activity.costInr > 0) currency.format(activity.costInr)
          else activity.costTag,
        style =
          MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
          ),
        color =
          if (activity.isFree) Color(0xFF00513B)
          else if (activity.isReserved) Color(0xFF783200)
          else Primary,
      )
    }
  }
}

@Composable
private fun AddActivityDialog(
  onDismiss: () -> Unit,
  onConfirm: (dayNumber: Int, title: String, time: String, cost: Int, desc: String) -> Unit,
) {
  var title by remember { mutableStateOf("") }
  var time by remember { mutableStateOf("04:30 PM") }
  var costString by remember { mutableStateOf("500") }
  var desc by remember { mutableStateOf("Explore local bazaar & architecture.") }
  var selectedDay by remember { mutableStateOf(1) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Add Trip Activity / Expense",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(text = "Assign to:", style = MaterialTheme.typography.labelMedium)
          Box(
            modifier =
              Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (selectedDay == 1) Primary else SurfaceContainer)
                .clickable { selectedDay = 1 }
                .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "Day 1",
              color = if (selectedDay == 1) Color.White else MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.Bold,
            )
          }
          Box(
            modifier =
              Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (selectedDay == 2) Primary else SurfaceContainer)
                .clickable { selectedDay = 2 }
                .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "Day 2",
              color = if (selectedDay == 2) Color.White else MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.Bold,
            )
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Activity Title (e.g. Bapu Bazaar Shopping)") },
          textStyle =
            MaterialTheme.typography.bodyMedium.copy(
              color = Color(0xFF131B2E),
              fontWeight = FontWeight.SemiBold,
            ),
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color(0xFF131B2E),
              unfocusedTextColor = Color(0xFF131B2E),
              cursorColor = Primary,
            ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time") },
            textStyle =
              MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF131B2E),
                fontWeight = FontWeight.SemiBold,
              ),
            colors =
              OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF131B2E),
                unfocusedTextColor = Color(0xFF131B2E),
                cursorColor = Primary,
              ),
            singleLine = true,
            modifier = Modifier.weight(1f),
          )
          OutlinedTextField(
            value = costString,
            onValueChange = { costString = it },
            label = { Text("Cost (₹)") },
            textStyle =
              MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF131B2E),
                fontWeight = FontWeight.SemiBold,
              ),
            colors =
              OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF131B2E),
                unfocusedTextColor = Color(0xFF131B2E),
                cursorColor = Primary,
              ),
            singleLine = true,
            modifier = Modifier.weight(1f),
          )
        }

        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Description & Notes") },
          textStyle =
            MaterialTheme.typography.bodyMedium.copy(
              color = Color(0xFF131B2E),
            ),
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color(0xFF131B2E),
              unfocusedTextColor = Color(0xFF131B2E),
              cursorColor = Primary,
            ),
          modifier = Modifier.fillMaxWidth(),
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val cost = costString.toIntOrNull() ?: 0
          onConfirm(selectedDay, title, time, cost, desc)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
      ) {
        Text("Add to Itinerary")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    },
  )
}

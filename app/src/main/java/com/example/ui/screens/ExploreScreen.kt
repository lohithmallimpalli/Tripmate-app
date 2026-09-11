package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Currency
import com.example.data.Destination
import com.example.data.TripMateRepository
import com.example.ui.TripMateUiState
import com.example.ui.TripMateViewModel
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.StarGold
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
  uiState: TripMateUiState,
  viewModel: TripMateViewModel,
  onNavigateToDetails: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 120.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      // 1. Search Pill Input
      item {
        Box(
          modifier =
            Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
          OutlinedTextField(
            value = uiState.exploreSearchQuery,
            onValueChange = { viewModel.updateExploreSearch(it) },
            placeholder = {
              Text(
                "Search destinations, states, festivals...",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = Color(0xFF6E797E),
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Primary,
              )
            },
            trailingIcon = {
              if (uiState.exploreSearchQuery.isNotEmpty()) {
                IconButton(onClick = { viewModel.updateExploreSearch("") }) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = Color(0xFF131B2E),
                  )
                }
              }
            },
            textStyle =
              MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF131B2E), // Deep dark font for typed text
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
              ),
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors =
              OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF131B2E), // Explicit dark text
                unfocusedTextColor = Color(0xFF131B2E), // Explicit dark text
                cursorColor = Primary,
                focusedPlaceholderColor = Color(0xFF6E797E),
                unfocusedPlaceholderColor = Color(0xFF6E797E),
                focusedBorderColor = Primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = SurfaceContainerLowest,
                unfocusedContainerColor = SurfaceContainerLowest,
              ),
            modifier = Modifier.fillMaxWidth(),
          )
        }
      }

      // 2. Filter Group 1: Indian Regions
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "REGIONS OF INDIA",
              style =
                MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                ),
              color = Color(0xFF45464F),
            )
            if (uiState.selectedRegions.isNotEmpty() || uiState.selectedStyles.isNotEmpty()) {
              Row(
                modifier =
                  Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.clearFilters() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.RestartAlt,
                  contentDescription = "Reset filters",
                  tint = Primary,
                  modifier = Modifier.size(14.dp),
                )
                Text(
                  text = "Reset",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = Primary,
                )
              }
            }
          }

          val regions =
            listOf(
              "North India",
              "South India",
              "West & Goa",
              "Northeast & East",
              "Himalayas",
            )
          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            items(regions) { region ->
              val isSelected = uiState.selectedRegions.contains(region)
              Box(
                modifier =
                  Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) Primary else Color.White)
                    .border(
                      BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) Primary else Color(0xFFD0D5DD),
                      ),
                      shape = RoundedCornerShape(20.dp),
                    )
                    .clickable { viewModel.toggleRegion(region) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  if (isSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selected",
                      tint = Color.White,
                      modifier = Modifier.size(15.dp),
                    )
                  }
                  Text(
                    text = region,
                    style =
                      MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                      ),
                    color = if (isSelected) Color.White else Color(0xFF1E293B),
                  )
                }
              }
            }
          }
        }
      }

      // 3. Filter Group 2: Travel Style
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "TRAVEL STYLE",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
              ),
            color = Color(0xFF45464F),
            modifier = Modifier.padding(horizontal = 16.dp),
          )

          val styles = listOf("Solo Backpacker", "Spiritual / Heritage", "Family & Leisure")
          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            items(styles) { style ->
              val isSelected = uiState.selectedStyles.contains(style)
              Box(
                modifier =
                  Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) SecondaryContainer else Color.White)
                    .border(
                      BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) SecondaryContainer else Color(0xFFD0D5DD),
                      ),
                      shape = RoundedCornerShape(20.dp),
                    )
                    .clickable { viewModel.toggleStyle(style) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  if (isSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selected",
                      tint = Color.White,
                      modifier = Modifier.size(15.dp),
                    )
                  }
                  Text(
                    text = style,
                    style =
                      MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                      ),
                    color = if (isSelected) Color.White else Color(0xFF1E293B),
                  )
                }
              }
            }
          }
        }
      }

      // 4. Interactive Budget Window Module
      item {
        TripBudgetWindowCard(
          maxBudget = uiState.maxBudgetLimit,
          currency = uiState.currency,
          onBudgetChanged = { viewModel.updateMaxBudget(it) },
        )
      }

      // 5. Sorting Chips
      item {
        val sorts = listOf("🔥 Most Popular", "⭐ Highest Rated", "💰 Budget Friendly", "✨ Recommended")
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          items(sorts) { sort ->
            val isSelected = uiState.selectedSort == sort
            Box(
              modifier =
                Modifier
                  .clip(RoundedCornerShape(16.dp))
                  .background(if (isSelected) SurfaceContainerHigh else Color.White)
                  .border(
                    BorderStroke(
                      width = if (isSelected) 1.5.dp else 1.dp,
                      color = if (isSelected) Primary else Color(0xFFD0D5DD),
                    ),
                    shape = RoundedCornerShape(16.dp),
                  )
                  .clickable { viewModel.selectSort(sort) }
                  .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
              Text(
                text = sort,
                style =
                  MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  ),
                color = if (isSelected) Primary else Color(0xFF1E293B),
              )
            }
          }
        }
      }

      // 6. Destination Feed Cards (Vertical Stack)
      val filteredDestinations =
        TripMateRepository.destinations.filter { dest ->
          val matchesSearch =
            uiState.exploreSearchQuery.isBlank() ||
              dest.title.contains(uiState.exploreSearchQuery, ignoreCase = true) ||
              dest.state.contains(uiState.exploreSearchQuery, ignoreCase = true) ||
              dest.region.contains(uiState.exploreSearchQuery, ignoreCase = true) ||
              dest.tags.any { it.contains(uiState.exploreSearchQuery, ignoreCase = true) }
          val matchesRegion =
            uiState.selectedRegions.isEmpty() || uiState.selectedRegions.contains(dest.region)
          val matchesStyle =
            uiState.selectedStyles.isEmpty() ||
              (uiState.selectedStyles.contains("Solo Backpacker") && dest.isBackpacker) ||
              (uiState.selectedStyles.contains("Spiritual / Heritage") && dest.isSpiritual) ||
              (uiState.selectedStyles.contains("Family & Leisure") && dest.isFamily)
          val matchesBudget = dest.priceInr <= uiState.maxBudgetLimit

          matchesSearch && matchesRegion && matchesStyle && matchesBudget
        }

      val sortedDestinations =
        when (uiState.selectedSort) {
          "⭐ Highest Rated" -> filteredDestinations.sortedByDescending { it.rating }
          "💰 Budget Friendly" -> filteredDestinations.sortedBy { it.priceInr }
          "🔥 Most Popular" -> filteredDestinations.sortedByDescending { it.reviewCount }
          else -> filteredDestinations
        }

      // Destination count banner
      item {
        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "Showing ${sortedDestinations.size} destinations",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF6E797E),
          )
        }
      }

      if (sortedDestinations.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
          ) {
            Column(
              modifier = Modifier.fillMaxWidth().padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(44.dp),
              )
              Text(
                text = "No destinations match your filters",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF131B2E),
              )
              Text(
                text = "Try adjusting your budget limit, clearing region filters, or resetting your search.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5A6472),
                textAlign = TextAlign.Center,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Button(
                onClick = { viewModel.clearFilters() },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(12.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.RestartAlt,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reset All Filters")
              }
            }
          }
        }
      }

      items(sortedDestinations) { dest ->
        ExploreDestinationCard(
          destination = dest,
          currency = uiState.currency,
          isFavorite = uiState.favoriteDestinationIds.contains(dest.id),
          onToggleFavorite = { viewModel.toggleFavorite(dest.id) },
          onExploreNow = { onNavigateToDetails(dest.id) },
        )
      }
    }

    // Floating Filters Button at bottom center showing active filter count
    val activeFilterCount = uiState.selectedRegions.size + uiState.selectedStyles.size
    if (activeFilterCount > 0) {
      Surface(
        modifier =
          Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 80.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF131B2E),
        shadowElevation = 6.dp,
      ) {
        Row(
          modifier =
            Modifier
              .clickable { viewModel.clearFilters() }
              .padding(horizontal = 18.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Filters",
            tint = Color.White,
            modifier = Modifier.size(18.dp),
          )
          Text(
            text = "$activeFilterCount Active Filter${if (activeFilterCount > 1) "s" else ""} • Clear",
            style =
              MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
              ),
          )
        }
      }
    }
  }
}

@Composable
private fun TripBudgetWindowCard(
  maxBudget: Int,
  currency: Currency,
  onBudgetChanged: (Int) -> Unit,
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
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = "Trip Budget Window",
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text = "Per person total estimated trip expense",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        Box(
          modifier =
            Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(TertiaryFixed)
              .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "186 found",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00513B),
              ),
          )
        }
      }

      // Live Slider
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Min: ${currency.format(10000)}",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = MaterialTheme.colorScheme.outline,
        )
        Text(
          text = "Up to: ${currency.format(maxBudget)}",
          style =
            MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = Primary,
            ),
        )
      }

      Slider(
        value = maxBudget.toFloat(),
        onValueChange = { onBudgetChanged(it.toInt()) },
        valueRange = 10000f..150000f,
        steps = 14,
        colors =
          SliderDefaults.colors(
            thumbColor = Primary,
            activeTrackColor = Primary,
            inactiveTrackColor = SurfaceContainerHigh,
          ),
        modifier = Modifier.fillMaxWidth(),
      )

      // Range labels
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = "Hostel & Backpacking",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = MaterialTheme.colorScheme.outline,
        )
        Text(
          text = "Balanced Comfort",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = MaterialTheme.colorScheme.outline,
        )
        Text(
          text = "Boutique Luxury",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = MaterialTheme.colorScheme.outline,
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreDestinationCard(
  destination: Destination,
  currency: Currency,
  isFavorite: Boolean,
  onToggleFavorite: () -> Unit,
  onExploreNow: () -> Unit,
) {
  Card(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column {
      // Photo Header
      Box(
        modifier =
          Modifier
            .fillMaxWidth()
            .height(180.dp)
      ) {
        AsyncImage(
          model = destination.imageUrl,
          contentDescription = destination.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop,
        )

        // Top Badges
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier =
              Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.92f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = destination.badge,
              style =
                MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                ),
              color = Color(0xFF131B2E),
            )
          }

          IconButton(
            onClick = onToggleFavorite,
            modifier =
              Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.92f)),
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Save",
              tint = if (isFavorite) SecondaryContainer else MaterialTheme.colorScheme.outline,
              modifier = Modifier.size(18.dp),
            )
          }
        }
      }

      // Body Details
      Column(
        modifier = Modifier.padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = destination.title,
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = StarGold,
              modifier = Modifier.size(16.dp),
            )
            Text(
              text = "${destination.rating}",
              style =
                MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                ),
              color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
              text = "(${destination.reviewCount})",
              style =
                MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.outline,
                ),
            )
          }
        }

        // Tags Flow
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          destination.tags.forEach { tag ->
            Box(
              modifier =
                Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainer)
                  .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = tag,
                style =
                  MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                  ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }

        // Bottom Price & Explore Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column {
            Text(
              text = "Est. ${destination.durationDays} Days Package",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
              color = MaterialTheme.colorScheme.outline,
            )
            Text(
              text = currency.format(destination.priceInr),
              style =
                MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 20.sp,
                ),
              color = Primary,
            )
          }

          Button(
            onClick = onExploreNow,
            colors =
              ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = OnPrimary,
              ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          ) {
            Text(
              text = "Explore Now",
              style =
                MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
            )
          }
        }
      }
    }
  }
}

package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Fort
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Currency
import com.example.data.Deal
import com.example.data.Destination
import com.example.data.LocalDish
import com.example.data.TripMateRepository
import com.example.ui.Screen
import com.example.ui.TripMateUiState
import com.example.ui.TripMateViewModel
import com.example.ui.theme.OnPrimaryFixed
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
fun HomeScreen(
  uiState: TripMateUiState,
  viewModel: TripMateViewModel,
  onNavigateToDetails: (String) -> Unit,
  onNavigateToPlanner: () -> Unit,
  onNavigateToExplore: () -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 90.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // 1. Greeting Header Section
    item {
      Column(
        modifier =
          Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = "Explore Incredible India, Alex!",
            style =
              MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text = "✨",
            fontSize = 20.sp,
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "DISCOVER MORE. SPEND WISELY. TRAVEL BETTER.",
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
            ),
          color = Primary,
        )
      }
    }

    // 2. Hero Visual Banner with Integrated Floating Search
    item {
      HeroBannerSection(
        searchQuery = uiState.homeSearchQuery,
        onSearchChange = { viewModel.updateHomeSearch(it) },
        onFilterClick = onNavigateToExplore,
      )
    }

    // Instant Search Results (when typing in the search bar)
    if (uiState.homeSearchQuery.isNotBlank()) {
      val matchingDestinations = TripMateRepository.destinations.filter {
        it.title.contains(uiState.homeSearchQuery, ignoreCase = true) ||
        it.subtitle.contains(uiState.homeSearchQuery, ignoreCase = true) ||
        it.state.contains(uiState.homeSearchQuery, ignoreCase = true) ||
        it.region.contains(uiState.homeSearchQuery, ignoreCase = true) ||
        it.description.contains(uiState.homeSearchQuery, ignoreCase = true)
      }
      item {
        Card(
          modifier =
            Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        ) {
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
                text = "Results for \"${uiState.homeSearchQuery}\"",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF131B2E),
              )
              Text(
                text = "${matchingDestinations.size} found",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Primary,
              )
            }

            if (matchingDestinations.isEmpty()) {
              Text(
                text = "No destinations matching \"${uiState.homeSearchQuery}\". Try \"Goa\", \"Ladakh\", \"Kerala\", or \"Jaipur\".",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6E797E),
                modifier = Modifier.padding(vertical = 6.dp),
              )
            } else {
              matchingDestinations.forEach { dest ->
                Row(
                  modifier =
                    Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(12.dp))
                      .clickable { onNavigateToDetails(dest.id) }
                      .background(SurfaceContainerLow)
                      .padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                  AsyncImage(
                    model = dest.imageUrl,
                    contentDescription = dest.title,
                    modifier =
                      Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                  )
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = dest.title,
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                      color = Color(0xFF131B2E),
                    )
                    Text(
                      text = "${dest.subtitle} • ${dest.state}",
                      style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                      color = Color(0xFF5A6472),
                      maxLines = 1,
                    )
                  }
                  Text(
                    text = "${uiState.currency.symbol}${dest.priceInr}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Primary,
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. Category Quick Chips
    item {
      CategoryChipsSection(
        selectedCategory = uiState.selectedCategory,
        onSelectCategory = { viewModel.selectCategory(it) },
      )
    }

    // 4. Featured Promo Card: Smart Trip Planner
    item {
      SmartPlannerPromoCard(onPlanTripClick = onNavigateToPlanner)
    }

    // 5. Section: Trending Destinations (Horizontal Carousel)
    item {
      TrendingDestinationsSection(
        destinations = TripMateRepository.destinations,
        currency = uiState.currency,
        favorites = uiState.favoriteDestinationIds,
        onToggleFavorite = { viewModel.toggleFavorite(it) },
        onDestinationClick = onNavigateToDetails,
        onSeeAllClick = onNavigateToExplore,
      )
    }

    // 6. Section: Best Trips for Your Budget
    item {
      BestTripsForBudgetSection(
        deals = TripMateRepository.deals,
        currency = uiState.currency,
        onViewDeal = { onNavigateToDetails("goa") },
      )
    }

    // 7. Section: Must-Try Local Dining
    item {
      MustTryDiningSection(
        dishes = TripMateRepository.localDishes,
        currency = uiState.currency,
      )
    }
  }
}

@Composable
private fun HeroBannerSection(
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  onFilterClick: () -> Unit,
) {
  Box(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(260.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
  ) {
    // Background photo of Leh Ladakh Pangong Tso
    AsyncImage(
      model = TripMateRepository.LADAKH_HERO_URL,
      contentDescription = "Leh Ladakh Pangong Tso Lake",
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )

    // Dark scrim gradient overlay
    Box(
      modifier =
        Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors =
                listOf(
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.35f),
                  Color.Black.copy(alpha = 0.88f),
                )
            )
          )
    )

    // Content container
    Column(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(14.dp),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      // Top pill indicators
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          modifier =
            Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(Color.White.copy(alpha = 0.92f))
              .padding(horizontal = 10.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = "Trending",
            tint = SecondaryContainer,
            modifier = Modifier.size(16.dp),
          )
          Text(
            text = "Trending in India",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
              ),
            color = Color(0xFF131B2E),
          )
        }

        Box(
          modifier =
            Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(Primary.copy(alpha = 0.85f))
              .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = "150+ Desi Guides",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
              ),
            color = Color.White,
          )
        }
      }

      // Headline & Floating Search Strip
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column {
          Text(
            text = "YOUR NEXT INDIAN GETAWAY",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
              ),
            color = PrimaryFixed,
          )
          Text(
            text = "Discover Incredible India",
            style =
              MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
              ),
            color = Color.White,
          )
        }

        // Search Bar Strip
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White)
              .padding(horizontal = 10.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Primary,
            modifier = Modifier.size(20.dp),
          )
          Spacer(modifier = Modifier.width(4.dp))
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
              Text(
                text = "Search Goa, Ladakh, Kerala, Jaipur...",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = Color(0xFF6E797E),
                maxLines = 1,
              )
            },
            textStyle =
              MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF131B2E), // Deep dark font for typed text
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
              ),
            modifier = Modifier.weight(1f),
            singleLine = true,
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(
                  onClick = { onSearchChange("") },
                  modifier = Modifier.size(28.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = Color(0xFF131B2E),
                    modifier = Modifier.size(16.dp),
                  )
                }
              }
            },
            colors =
              OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF131B2E), // High contrast dark text
                unfocusedTextColor = Color(0xFF131B2E), // High contrast dark text
                cursorColor = Primary,
                focusedPlaceholderColor = Color(0xFF6E797E),
                unfocusedPlaceholderColor = Color(0xFF6E797E),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
              ),
          )
          IconButton(
            onClick = onFilterClick,
            modifier =
              Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainer),
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Filters",
              tint = Primary,
              modifier = Modifier.size(18.dp),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CategoryChipsSection(
  selectedCategory: String,
  onSelectCategory: (String) -> Unit,
) {
  val categories =
    listOf(
      Triple("all", "All India", Icons.Default.Explore),
      Triple("goa", "Goa", Icons.Default.BeachAccess),
      Triple("ladakh", "Ladakh", Icons.Default.Landscape),
      Triple("rajasthan", "Rajasthan", Icons.Default.Fort),
      Triple("kerala", "Kerala", Icons.Default.Sailing),
      Triple("himachal", "Himachal", Icons.Default.DownhillSkiing),
    )

  LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(categories) { (id, label, icon) ->
      val isSelected = selectedCategory == id
      Row(
        modifier =
          Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) Primary else SurfaceContainer)
            .clickable { onSelectCategory(id) }
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = if (isSelected) Color.White else Primary,
          modifier = Modifier.size(18.dp),
        )
        Text(
          text = label,
          style =
            MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp,
            ),
          color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
        )
      }
    }
  }
}

@Composable
private fun SmartPlannerPromoCard(onPlanTripClick: () -> Unit) {
  Box(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(
          Brush.linearGradient(
            colors = listOf(SurfaceContainerLowest, SurfaceContainerLow, SurfaceContainer)
          )
        )
        .padding(18.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Box(
          modifier =
            Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(SecondaryContainer),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = "Smart Trip Planner",
            tint = Color.White,
            modifier = Modifier.size(16.dp),
          )
        }
        Text(
          text = "SMART TRIP PLANNER",
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
            ),
          color = Secondary,
        )
      }

      Text(
        text = "Auto-generate realistic itineraries within your exact budget",
        style =
          MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
          ),
        color = MaterialTheme.colorScheme.onSurface,
      )

      Text(
        text =
          "Pick your dates & wallet limits; our AI calculates flights, lodging, tickets, and dining down to the rupee.",
        style =
          MaterialTheme.typography.bodySmall.copy(
            fontSize = 13.sp,
            lineHeight = 18.sp,
          ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Accuracy",
            tint = Tertiary,
            modifier = Modifier.size(18.dp),
          )
          Text(
            text = "98% Budget Accuracy",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
              ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Button(
          onClick = onPlanTripClick,
          colors =
            ButtonDefaults.buttonColors(
              containerColor = SecondaryContainer,
              contentColor = Color.White,
            ),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        ) {
          Text(
            text = "Plan My Trip",
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

@Composable
private fun TrendingDestinationsSection(
  destinations: List<Destination>,
  currency: Currency,
  favorites: Set<String>,
  onToggleFavorite: (String) -> Unit,
  onDestinationClick: (String) -> Unit,
  onSeeAllClick: () -> Unit,
) {
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
          text = "Trending Destinations",
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = "Most bookmarked by travelers across India",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Text(
        text = "See All >",
        style =
          MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Primary,
          ),
        modifier = Modifier.clickable(onClick = onSeeAllClick),
      )
    }

    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      items(destinations.take(4)) { dest ->
        DestinationTrendingCard(
          destination = dest,
          currency = currency,
          isFavorite = favorites.contains(dest.id),
          onToggleFavorite = { onToggleFavorite(dest.id) },
          onClick = { onDestinationClick(dest.id) },
        )
      }
    }
  }
}

@Composable
fun DestinationTrendingCard(
  destination: Destination,
  currency: Currency,
  isFavorite: Boolean,
  onToggleFavorite: () -> Unit,
  onClick: () -> Unit,
) {
  Card(
    modifier =
      Modifier
        .width(270.dp)
        .clickable(onClick = onClick),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column {
      // Photo Header with Badges
      Box(
        modifier =
          Modifier
            .fillMaxWidth()
            .height(160.dp)
      ) {
        AsyncImage(
          model = destination.imageUrl,
          contentDescription = destination.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop,
        )

        // Category Tag
        Box(
          modifier =
            Modifier
              .align(Alignment.TopStart)
              .padding(10.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(Color.White.copy(alpha = 0.92f))
              .padding(horizontal = 9.dp, vertical = 4.dp)
        ) {
          Text(
            text = destination.badge,
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
              ),
            color = Color(0xFF131B2E),
          )
        }

        // Favorite Heart Button
        IconButton(
          onClick = onToggleFavorite,
          modifier =
            Modifier
              .align(Alignment.TopEnd)
              .padding(8.dp)
              .size(34.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.92f)),
        ) {
          Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Save to favorites",
            tint = if (isFavorite) SecondaryContainer else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(18.dp),
          )
        }
      }

      // Card Body
      Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
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
                fontSize = 15.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = "Rating",
              tint = StarGold,
              modifier = Modifier.size(15.dp),
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

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          Icon(
            imageVector = Icons.Default.Fort,
            contentDescription = null,
            tint = Secondary,
            modifier = Modifier.size(14.dp),
          )
          Text(
            text = "Best: ${destination.seasonBadge}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        // Package Price Banner
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(SurfaceContainerLow)
              .padding(horizontal = 10.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column {
            Text(
              text = "Est. Package",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = MaterialTheme.colorScheme.outline,
            )
            Text(
              text = currency.format(destination.priceInr),
              style =
                MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                ),
              color = Primary,
            )
          }
          Text(
            text = "${destination.durationDays} days total",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  }
}

@Composable
private fun BestTripsForBudgetSection(
  deals: List<Deal>,
  currency: Currency,
  onViewDeal: () -> Unit,
) {
  Column(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text(
            text = "Best Trips for Your Budget",
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Box(
            modifier =
              Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(TertiaryFixed)
                .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "₹25,000 - ₹60,000",
              style =
                MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                ),
              color = Color(0xFF00513B),
            )
          }
        }
        Text(
          text = "Optimal flights, transit & hotel combos right now",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // Stacked Deal Cards
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      deals.forEach { deal ->
        DealItemCard(deal = deal, currency = currency, onViewDeal = onViewDeal)
      }
    }
  }
}

@Composable
private fun DealItemCard(
  deal: Deal,
  currency: Currency,
  onViewDeal: () -> Unit,
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        AsyncImage(
          model = deal.imageUrl,
          contentDescription = deal.title,
          modifier =
            Modifier
              .size(64.dp)
              .clip(RoundedCornerShape(12.dp)),
          contentScale = ContentScale.Crop,
        )
        Column {
          Text(
            text = deal.title,
            style =
              MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            Icon(
              imageVector =
                if (deal.transportType == "flight") Icons.Default.Flight
                else Icons.Default.DirectionsBus,
              contentDescription = null,
              tint = Tertiary,
              modifier = Modifier.size(14.dp),
            )
            Text(
              text = deal.transportCost,
              style =
                MaterialTheme.typography.bodySmall.copy(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                ),
              color = Tertiary,
            )
            Text(
              text = "•",
              color = MaterialTheme.colorScheme.outline,
            )
            Text(
              text = deal.roomRate,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          Text(
            text = "Estimated total: ~${currency.format(deal.totalEstimatedInr)}",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
              ),
            color = Secondary,
          )
        }
      }

      Button(
        onClick = onViewDeal,
        shape = RoundedCornerShape(10.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = PrimaryFixed,
            contentColor = OnPrimaryFixed,
          ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
      ) {
        Text(
          text = "View Deal",
          style =
            MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
            ),
        )
      }
    }
  }
}

@Composable
private fun MustTryDiningSection(
  dishes: List<LocalDish>,
  currency: Currency,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
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
          text = "Must-Try Local Dining",
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = "Top-rated regional Indian culinary discoveries",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Text(
        text = "Explore >",
        style =
          MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Primary,
          ),
      )
    }

    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      items(dishes) { dish ->
        DiningDishCard(dish = dish, currency = currency)
      }
    }
  }
}

@Composable
fun DiningDishCard(
  dish: LocalDish,
  currency: Currency,
) {
  Card(
    modifier = Modifier.width(180.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
  ) {
    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Box(
        modifier =
          Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
      ) {
        AsyncImage(
          model = dish.imageUrl,
          contentDescription = dish.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop,
        )
        Box(
          modifier =
            Modifier
              .align(Alignment.BottomStart)
              .padding(6.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(Color.Black.copy(alpha = 0.75f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "~${currency.format(dish.priceInr)} / ${dish.priceSuffix}",
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
              ),
            color = Color.White,
          )
        }
      }

      Text(
        text = dish.title,
        style =
          MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          ),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = dish.location,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
      ) {
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = null,
          tint = StarGold,
          modifier = Modifier.size(13.dp),
        )
        Text(
          text = "${dish.rating}",
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = "(${dish.reviewCount})",
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              color = MaterialTheme.colorScheme.outline,
            ),
        )
      }
    }
  }
}

package com.example.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.data.Attraction
import com.example.data.BudgetTier
import com.example.data.Currency
import com.example.data.Destination
import com.example.data.LocalDish
import com.example.data.RecommendedStay
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
fun DestinationDetailsScreen(
  destinationId: String,
  uiState: TripMateUiState,
  viewModel: TripMateViewModel,
  onBackClick: () -> Unit,
  onStartPlanning: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val destination = viewModel.getDestination(destinationId)
  val isFav = uiState.favoriteDestinationIds.contains(destination.id)

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 120.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      // 1. Hero Image with Badges & Title
      item {
        HeroHeaderWithTitle(
          destination = destination,
          isFavorite = isFav,
          onToggleFavorite = { viewModel.toggleFavorite(destination.id) },
          onBackClick = onBackClick,
        )
      }

      // 2. 4-Stat Grid Bar
      item {
        StatsGridBar(destination = destination)
      }

      // 3. Quick Action Buttons Ribbon
      item {
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Button(
            onClick = { viewModel.toggleFavorite(destination.id) },
            modifier = Modifier.weight(1f),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = if (isFav) PrimaryFixed else SurfaceContainer,
                contentColor = if (isFav) Color(0xFF001F28) else MaterialTheme.colorScheme.onSurface,
              ),
            shape = RoundedCornerShape(12.dp),
          ) {
            Icon(
              imageVector = if (isFav) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isFav) "Saved Guide" else "Save Guide",
              fontWeight = FontWeight.Bold,
            )
          }

          Button(
            onClick = onStartPlanning,
            modifier = Modifier.weight(1.2f),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = SecondaryContainer,
                contentColor = Color.White,
              ),
            shape = RoundedCornerShape(12.dp),
          ) {
            Icon(
              imageVector = Icons.Default.CardTravel,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Plan ${destination.title.substringBefore(",")}",
              fontWeight = FontWeight.Bold,
              maxLines = 1,
            )
          }
        }
      }

      // 4. Overview Section
      item {
        OverviewSection(destination = destination)
      }

      // 5. Daily Budget Planner (Interactive Tiers)
      item {
        DailyBudgetPlannerSection(
          tiers = TripMateRepository.varanasiBudgetTiers,
          selectedTierId = uiState.selectedTierId,
          currency = uiState.currency,
          onSelectTier = { viewModel.selectTier(it) },
        )
      }

      // 6. Must-Visit Attractions Carousel
      item {
        AttractionsSection(attractions = TripMateRepository.varanasiAttractions)
      }

      // 7. Banarasi Gastronomy Section
      item {
        GastronomySection(
          dishes = TripMateRepository.varanasiDishes,
          currency = uiState.currency,
        )
      }

      // 8. Recommended Stay
      item {
        RecommendedStayCard(
          stay = TripMateRepository.varanasiStay,
          currency = uiState.currency,
        )
      }

      // 9. Interactive Map Section
      item {
        InteractiveMapCard(destination = destination)
      }
    }

    // Sticky Bottom Floating Action Drawer
    Surface(
      modifier =
        Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth(),
      color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
      shadowElevation = 12.dp,
    ) {
      Row(
        modifier =
          Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Start Trip",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.outline,
          )
          Text(
            text = "${destination.title.substringBefore(",")} Experience",
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }

        IconButton(
          onClick = { viewModel.toggleFavorite(destination.id) },
          modifier =
            Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceContainer),
        ) {
          Icon(
            imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFav) SecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Button(
          onClick = onStartPlanning,
          shape = RoundedCornerShape(12.dp),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = Primary,
              contentColor = OnPrimary,
            ),
          contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        ) {
          Text(
            text = "Start Planning",
            style =
              MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
              ),
          )
          Spacer(modifier = Modifier.width(6.dp))
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
private fun HeroHeaderWithTitle(
  destination: Destination,
  isFavorite: Boolean,
  onToggleFavorite: () -> Unit,
  onBackClick: () -> Unit,
) {
  Box(
    modifier =
      Modifier
        .fillMaxWidth()
        .height(310.dp)
  ) {
    AsyncImage(
      model = destination.imageUrl,
      contentDescription = destination.title,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )

    // Gradient scrim
    Box(
      modifier =
        Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors =
                listOf(
                  Color.Black.copy(alpha = 0.5f),
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.85f),
                )
            )
          )
    )

    // Top action bar
    Row(
      modifier =
        Modifier
          .fillMaxWidth()
          .padding(top = 40.dp, start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      IconButton(
        onClick = onBackClick,
        modifier =
          Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.45f)),
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White,
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(
          onClick = onToggleFavorite,
          modifier =
            Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.45f)),
        ) {
          Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) SecondaryContainer else Color.White,
          )
        }
      }
    }

    // Bottom info overlay
    Column(
      modifier =
        Modifier
          .align(Alignment.BottomStart)
          .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Box(
        modifier =
          Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SecondaryContainer)
            .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text(
          text = "Top Spiritual Pick 2025",
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
            ),
          color = Color.White,
        )
      }

      Text(
        text = destination.title,
        style =
          MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
          ),
        color = Color.White,
      )

      Text(
        text = destination.subtitle,
        style =
          MaterialTheme.typography.bodyMedium.copy(
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.Medium,
          ),
      )

      Text(
        text = "INDIA • Ganga Aarti & Ghats",
        style =
          MaterialTheme.typography.labelSmall.copy(
            letterSpacing = 1.sp,
            color = PrimaryFixed,
          ),
      )
    }
  }
}

@Composable
private fun StatsGridBar(destination: Destination) {
  Row(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(SurfaceContainerLowest)
        .padding(12.dp),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    StatItem(
      icon = Icons.Default.Star,
      iconColor = StarGold,
      primaryText = "${destination.rating}",
      secondaryText = "(${destination.reviewCount})",
    )
    StatItem(
      icon = Icons.Default.CalendarMonth,
      iconColor = Primary,
      primaryText = destination.seasonBadge,
      secondaryText = "Best Season",
    )
    StatItem(
      icon = Icons.Default.Thermostat,
      iconColor = Secondary,
      primaryText = destination.weather.substringBefore(" /"),
      secondaryText = "Pleasant",
    )
    StatItem(
      icon = Icons.Default.Timer,
      iconColor = Tertiary,
      primaryText = destination.idealDuration,
      secondaryText = "Ideal Stay",
    )
  }
}

@Composable
private fun StatItem(
  icon: ImageVector,
  iconColor: Color,
  primaryText: String,
  secondaryText: String,
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = iconColor,
      modifier = Modifier.size(20.dp),
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = primaryText,
      style =
        MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
        ),
      color = MaterialTheme.colorScheme.onSurface,
    )
    Text(
      text = secondaryText,
      style =
        MaterialTheme.typography.labelSmall.copy(
          fontSize = 10.sp,
        ),
      color = MaterialTheme.colorScheme.outline,
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OverviewSection(destination: Destination) {
  Column(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(
      text = "About the Eternal City of Light",
      style =
        MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
        ),
      color = MaterialTheme.colorScheme.onSurface,
    )

    Text(
      text = destination.description,
      style =
        MaterialTheme.typography.bodyMedium.copy(
          fontSize = 14.sp,
          lineHeight = 22.sp,
        ),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(top = 4.dp),
    ) {
      listOf("UNESCO Heritage City", "Spiritual Capital", "Ancient Ghats", "Silk Weaving").forEach { badge ->
        Box(
          modifier =
            Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(SurfaceContainer)
              .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = badge,
            style =
              MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
              ),
            color = Primary,
          )
        }
      }
    }
  }
}

@Composable
private fun DailyBudgetPlannerSection(
  tiers: List<BudgetTier>,
  selectedTierId: String,
  currency: Currency,
  onSelectTier: (String) -> Unit,
) {
  Column(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column {
        Text(
          text = "Daily Budget Planner",
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = "Choose your travel comfort level for tailored plans",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      tiers.forEach { tier ->
        val isSelected = tier.id == selectedTierId
        Card(
          shape = RoundedCornerShape(14.dp),
          colors =
            CardDefaults.cardColors(
              containerColor =
                if (isSelected) PrimaryFixed.copy(alpha = 0.35f)
                else SurfaceContainerLowest
            ),
          border =
            if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Primary)
            else null,
          modifier =
            Modifier
              .fillMaxWidth()
              .clickable { onSelectTier(tier.id) },
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Box(
                modifier =
                  Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Primary else SurfaceContainer),
                contentAlignment = Alignment.Center,
              ) {
                Icon(
                  imageVector =
                    when (tier.iconType) {
                      "backpack" -> Icons.Default.Navigation
                      "hotel" -> Icons.Default.Hotel
                      else -> Icons.Default.Spa
                    },
                  contentDescription = null,
                  tint = if (isSelected) Color.White else Primary,
                  modifier = Modifier.size(20.dp),
                )
              }

              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = tier.title,
                    style =
                      MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                      ),
                    color = MaterialTheme.colorScheme.onSurface,
                  )
                  if (tier.isPopular) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier =
                        Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .background(SecondaryContainer)
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = "Most Popular",
                        style =
                          MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                          ),
                        color = Color.White,
                      )
                    }
                  }
                }
                Text(
                  text = tier.description,
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = currency.format(tier.costPerDayInr),
                style =
                  MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                  ),
                color = Primary,
              )
              Text(
                text = "/ day",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.outline,
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AttractionsSection(attractions: List<Attraction>) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Text(
      text = "Must-Visit Attractions",
      style =
        MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
        ),
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.padding(horizontal = 16.dp),
    )

    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      items(attractions) { item ->
        Card(
          modifier = Modifier.width(230.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        ) {
          Column {
            Box(
              modifier =
                Modifier
                  .fillMaxWidth()
                  .height(130.dp)
            ) {
              AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
              )
              Box(
                modifier =
                  Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = item.tag,
                  style =
                    MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 10.sp,
                    ),
                  color = Color.White,
                )
              }
            }

            Column(
              modifier = Modifier.padding(10.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
              Text(
                text = item.title,
                style =
                  MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                  ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
              Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
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
                  text = "${item.rating} (${item.reviewCount})",
                  style =
                    MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp,
                    ),
                  color = MaterialTheme.colorScheme.onSurface,
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun GastronomySection(
  dishes: List<LocalDish>,
  currency: Currency,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Text(
      text = "Banarasi Gastronomy",
      style =
        MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
        ),
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.padding(horizontal = 16.dp),
    )

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
private fun RecommendedStayCard(
  stay: RecommendedStay,
  currency: Currency,
) {
  Column(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(
      text = "Top Recommended Stay",
      style =
        MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
        ),
      color = MaterialTheme.colorScheme.onSurface,
    )

    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth(),
    ) {
      Column {
        Box(
          modifier =
            Modifier
              .fillMaxWidth()
              .height(140.dp)
        ) {
          AsyncImage(
            model = stay.imageUrl,
            contentDescription = stay.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
          )
          Box(
            modifier =
              Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Tertiary)
                .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = stay.badge,
              style =
                MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                ),
              color = Color.White,
            )
          }
        }

        Column(
          modifier = Modifier.padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          Text(
            text = stay.name,
            style =
              MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text = stay.location,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Text(
            text = stay.description,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.outline,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "From ${currency.format(stay.pricePerNightInr)} / night",
              style =
                MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Primary,
                ),
            )
            Button(
              onClick = { /* stay details */ },
              shape = RoundedCornerShape(8.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = PrimaryFixed,
                  contentColor = Color(0xFF001F28),
                ),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            ) {
              Text(
                text = "Check Rooms",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun InteractiveMapCard(destination: Destination) {
  Column(
    modifier =
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(
      text = "Interactive Ghats & City Map",
      style =
        MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
        ),
      color = MaterialTheme.colorScheme.onSurface,
    )

    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
      modifier = Modifier.fillMaxWidth(),
    ) {
      Box(
        modifier =
          Modifier
            .fillMaxWidth()
            .height(160.dp)
      ) {
        AsyncImage(
          model = TripMateRepository.MAP_VARANASI_URL,
          contentDescription = "Map of Varanasi",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop,
        )

        Surface(
          modifier =
            Modifier
              .align(Alignment.BottomCenter)
              .padding(12.dp),
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFF131B2E).copy(alpha = 0.9f),
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Map,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp),
            )
            Text(
              text = "Explore 84 Ghats & Temples on Map",
              style =
                MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                ),
            )
          }
        }
      }
    }
  }
}

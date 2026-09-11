package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TripMateRepository
import com.example.ui.TripMateUiState
import com.example.ui.TripMateViewModel
import com.example.ui.theme.Primary

@Composable
fun SavedScreen(
  uiState: TripMateUiState,
  viewModel: TripMateViewModel,
  onNavigateToDetails: (String) -> Unit,
  onNavigateToExplore: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val savedDestinations =
    TripMateRepository.destinations.filter { uiState.favoriteDestinationIds.contains(it.id) }

  if (savedDestinations.isEmpty()) {
    Box(
      modifier = modifier.fillMaxSize().padding(24.dp),
      contentAlignment = Alignment.Center,
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Icon(
          imageVector = Icons.Default.BookmarkBorder,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.outline,
          modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
          text = "No Saved Trips Yet",
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = "Bookmark dream destinations across India to review and plan later.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = onNavigateToExplore,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Primary),
        ) {
          Text("Explore India")
        }
      }
    }
  } else {
    LazyColumn(
      modifier = modifier.fillMaxSize(),
      contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      item {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
          Text(
            text = "Saved Destinations (${savedDestinations.size})",
            style =
              MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
              ),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text = "Your curated India bucket list & travel guides",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      items(savedDestinations) { dest ->
        ExploreDestinationCard(
          destination = dest,
          currency = uiState.currency,
          isFavorite = true,
          onToggleFavorite = { viewModel.toggleFavorite(dest.id) },
          onExploreNow = { onNavigateToDetails(dest.id) },
        )
      }
    }
  }
}

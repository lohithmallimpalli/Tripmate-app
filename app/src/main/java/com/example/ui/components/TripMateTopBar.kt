package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.TripMateRepository
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.SecondaryContainer

@Composable
fun TripMateTopBar(
  title: String = "TripMate",
  subtitle: String = "Home • India",
  showBackButton: Boolean = false,
  onBackClick: () -> Unit = {},
  onNotificationClick: () -> Unit = {},
  onProfileClick: () -> Unit = {},
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    shadowElevation = 2.dp,
  ) {
    Row(
      modifier =
        Modifier
          .fillMaxWidth()
          .height(60.dp)
          .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (showBackButton) {
        IconButton(
          onClick = onBackClick,
          modifier = Modifier.size(40.dp),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurface,
          )
        }
        Spacer(modifier = Modifier.width(4.dp))
      }

      // Brand Logo
      AsyncImage(
        model = TripMateRepository.LOGO_URL,
        contentDescription = "TripMate Logo",
        modifier =
          Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Fit,
      )

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
            ),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = subtitle,
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }

      // Notification icon with badge
      Box(
        modifier =
          Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onNotificationClick),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Default.Notifications,
          contentDescription = "Notifications",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(24.dp),
        )
        // Red / Amber badge dot
        Box(
          modifier =
            Modifier
              .size(8.dp)
              .align(Alignment.TopEnd)
              .padding(end = 2.dp, top = 2.dp)
              .background(SecondaryContainer, CircleShape)
              .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
        )
      }

      Spacer(modifier = Modifier.width(6.dp))

      // User profile picture
      AsyncImage(
        model = TripMateRepository.PROFILE_AVATAR_URL,
        contentDescription = "Profile",
        modifier =
          Modifier
            .size(36.dp)
            .clip(CircleShape)
            .border(2.dp, PrimaryFixed, CircleShape)
            .clickable(onClick = onProfileClick),
        contentScale = ContentScale.Crop,
      )
    }
  }
}

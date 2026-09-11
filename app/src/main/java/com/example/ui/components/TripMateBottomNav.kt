package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed

@Composable
fun TripMateBottomNav(
  currentScreen: Screen,
  onNavigate: (Screen) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    shadowElevation = 8.dp,
  ) {
    Row(
      modifier =
        Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .height(64.dp)
          .padding(horizontal = 8.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      NavTabItem(
        label = "Home",
        selected = currentScreen is Screen.Home,
        activeIcon = Icons.Filled.Home,
        inactiveIcon = Icons.Outlined.Home,
        onClick = { onNavigate(Screen.Home) },
      )
      NavTabItem(
        label = "Explore",
        selected = currentScreen is Screen.Explore,
        activeIcon = Icons.Filled.Explore,
        inactiveIcon = Icons.Outlined.Explore,
        onClick = { onNavigate(Screen.Explore) },
      )
      NavTabItem(
        label = "Planner",
        selected = currentScreen is Screen.Planner,
        activeIcon = Icons.Filled.CalendarMonth,
        inactiveIcon = Icons.Outlined.CalendarMonth,
        onClick = { onNavigate(Screen.Planner) },
      )
      NavTabItem(
        label = "Budget",
        selected = currentScreen is Screen.Budget,
        activeIcon = Icons.Filled.AccountBalanceWallet,
        inactiveIcon = Icons.Outlined.AccountBalanceWallet,
        onClick = { onNavigate(Screen.Budget) },
      )
      NavTabItem(
        label = "Saved",
        selected = currentScreen is Screen.Saved,
        activeIcon = Icons.Filled.Favorite,
        inactiveIcon = Icons.Outlined.FavoriteBorder,
        onClick = { onNavigate(Screen.Saved) },
      )
    }
  }
}

@Composable
private fun NavTabItem(
  label: String,
  selected: Boolean,
  activeIcon: ImageVector,
  inactiveIcon: ImageVector,
  onClick: () -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }

  Box(
    modifier =
      Modifier
        .height(48.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(
          if (selected) PrimaryFixed.copy(alpha = 0.45f) else Color.Transparent
        )
        .clickable(
          interactionSource = interactionSource,
          indication = ripple(bounded = true),
          onClick = onClick,
        )
        .padding(horizontal = 14.dp, vertical = 4.dp),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Icon(
        imageVector = if (selected) activeIcon else inactiveIcon,
        contentDescription = label,
        tint = if (selected) Primary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(22.dp),
      )
      Text(
        text = label,
        style =
          MaterialTheme.typography.labelSmall.copy(
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
          ),
        color = if (selected) Primary else MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

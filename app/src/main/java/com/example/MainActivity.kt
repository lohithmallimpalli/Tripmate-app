package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.Screen
import com.example.ui.TripMateViewModel
import com.example.ui.components.TripMateBottomNav
import com.example.ui.components.TripMateTopBar
import com.example.ui.screens.BudgetScreen
import com.example.ui.screens.DestinationDetailsScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlannerScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        TripMateApp()
      }
    }
  }
}

@Composable
fun TripMateApp(viewModel: TripMateViewModel = viewModel()) {
  val uiState by viewModel.uiState.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.feedbackMessage) {
    uiState.feedbackMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearFeedbackMessage()
    }
  }

  val (barTitle, barSubtitle, showBack) =
    when (val screen = uiState.currentScreen) {
      is Screen.Home -> Triple("TripMate", "Home • India", false)
      is Screen.Explore -> Triple("TripMate", "Explore India", false)
      is Screen.Details -> Triple("TripMate", "Destination Details", true)
      is Screen.Planner -> Triple("TripMate", "Planner", false)
      is Screen.Budget -> Triple("TripMate", "Budget & Expenses", false)
      is Screen.Saved -> Triple("TripMate", "Saved Trips", false)
    }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TripMateTopBar(
        title = barTitle,
        subtitle = barSubtitle,
        showBackButton = showBack,
        onBackClick = {
          if (uiState.currentScreen is Screen.Details) {
            viewModel.navigateTo(Screen.Explore)
          } else {
            viewModel.navigateTo(Screen.Home)
          }
        },
        onNotificationClick = {
          // notification feedback
        },
      )
    },
    bottomBar = {
      TripMateBottomNav(
        currentScreen = uiState.currentScreen,
        onNavigate = { viewModel.navigateTo(it) },
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) { innerPadding ->
    Box(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(innerPadding)
    ) {
      when (val screen = uiState.currentScreen) {
        is Screen.Home ->
          HomeScreen(
            uiState = uiState,
            viewModel = viewModel,
            onNavigateToDetails = { destId -> viewModel.navigateTo(Screen.Details(destId)) },
            onNavigateToPlanner = { viewModel.navigateTo(Screen.Planner) },
            onNavigateToExplore = { viewModel.navigateTo(Screen.Explore) },
          )

        is Screen.Explore ->
          ExploreScreen(
            uiState = uiState,
            viewModel = viewModel,
            onNavigateToDetails = { destId -> viewModel.navigateTo(Screen.Details(destId)) },
          )

        is Screen.Details ->
          DestinationDetailsScreen(
            destinationId = screen.destinationId,
            uiState = uiState,
            viewModel = viewModel,
            onBackClick = { viewModel.navigateTo(Screen.Explore) },
            onStartPlanning = { viewModel.navigateTo(Screen.Planner) },
          )

        is Screen.Planner ->
          PlannerScreen(
            uiState = uiState,
            viewModel = viewModel,
          )

        is Screen.Budget ->
          BudgetScreen(
            uiState = uiState,
            viewModel = viewModel,
          )

        is Screen.Saved ->
          SavedScreen(
            uiState = uiState,
            viewModel = viewModel,
            onNavigateToDetails = { destId -> viewModel.navigateTo(Screen.Details(destId)) },
            onNavigateToExplore = { viewModel.navigateTo(Screen.Explore) },
          )
      }
    }
  }
}

// Keep Greeting for test compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun TripMateAppPreview() {
  MyApplicationTheme {
    TripMateApp()
  }
}

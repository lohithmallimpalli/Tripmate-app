package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.Currency
import com.example.data.Destination
import com.example.data.ItineraryActivity
import com.example.data.ItineraryDay
import com.example.data.TripMateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface Screen {
  data object Home : Screen
  data object Explore : Screen
  data class Details(val destinationId: String = "varanasi") : Screen
  data object Planner : Screen
  data object Budget : Screen
  data object Saved : Screen
}

data class TripMateUiState(
  val currentScreen: Screen = Screen.Home,
  val currency: Currency = Currency.INR,
  val homeSearchQuery: String = "",
  val exploreSearchQuery: String = "",
  val selectedCategory: String = "all",
  val selectedRegions: Set<String> = emptySet(),
  val selectedStyles: Set<String> = emptySet(),
  val maxBudgetLimit: Int = 150000,
  val selectedSort: String = "Most Popular",
  val favoriteDestinationIds: Set<String> = setOf("jaipur", "ladakh"),
  val selectedTierId: String = "comfort",
  val isAiOptimized: Boolean = false,
  val itineraryDays: List<ItineraryDay> = TripMateRepository.jaipurItineraryDays,
  val showAddActivityDialog: Boolean = false,
  val feedbackMessage: String? = null,
  val notificationCount: Int = 3,
) {
  // Budget calculations
  val baseTotalBudget: Int = 90000
  val optimizationSavings: Int = if (isAiOptimized) 3500 else 0
  val lodgingCost: Int = 28000
  val transitCost: Int = if (isAiOptimized) 13000 else 16500
  val foodCost: Int = 11200
  val activitiesCost: Int = if (isAiOptimized) 8500 else 8500

  val totalSpent: Int = lodgingCost + transitCost + foodCost + activitiesCost
  val remainingBudget: Int = baseTotalBudget - totalSpent
  val spentPercentage: Int = ((totalSpent.toDouble() / baseTotalBudget) * 100).toInt()
  val savingsPercentage: Int = 100 - spentPercentage
}

class TripMateViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(TripMateUiState())
  val uiState: StateFlow<TripMateUiState> = _uiState.asStateFlow()

  fun navigateTo(screen: Screen) {
    _uiState.update { it.copy(currentScreen = screen) }
  }

  fun selectCurrency(currency: Currency) {
    _uiState.update { it.copy(currency = currency) }
  }

  fun updateHomeSearch(query: String) {
    _uiState.update { it.copy(homeSearchQuery = query) }
  }

  fun updateExploreSearch(query: String) {
    _uiState.update { it.copy(exploreSearchQuery = query) }
  }

  fun selectCategory(category: String) {
    _uiState.update { it.copy(selectedCategory = category) }
  }

  fun toggleRegion(region: String) {
    _uiState.update { state ->
      val current = state.selectedRegions.toMutableSet()
      if (current.contains(region)) {
        current.remove(region)
      } else {
        current.add(region)
      }
      state.copy(selectedRegions = current)
    }
  }

  fun toggleStyle(style: String) {
    _uiState.update { state ->
      val current = state.selectedStyles.toMutableSet()
      if (current.contains(style)) {
        current.remove(style)
      } else {
        current.add(style)
      }
      state.copy(selectedStyles = current)
    }
  }

  fun clearFilters() {
    _uiState.update {
      it.copy(
        selectedRegions = emptySet(),
        selectedStyles = emptySet(),
        maxBudgetLimit = 150000,
        exploreSearchQuery = "",
      )
    }
  }

  fun updateMaxBudget(budget: Int) {
    _uiState.update { it.copy(maxBudgetLimit = budget) }
  }

  fun selectSort(sort: String) {
    _uiState.update { it.copy(selectedSort = sort) }
  }

  fun toggleFavorite(destinationId: String) {
    _uiState.update { state ->
      val favorites = state.favoriteDestinationIds.toMutableSet()
      val isNowFav = if (favorites.contains(destinationId)) {
        favorites.remove(destinationId)
        false
      } else {
        favorites.add(destinationId)
        true
      }
      val destName = TripMateRepository.destinations.find { it.id == destinationId }?.title?.substringBefore(",") ?: "Destination"
      val msg = if (isNowFav) "Saved $destName to your Wishlist!" else "Removed $destName from Wishlist."
      state.copy(favoriteDestinationIds = favorites, feedbackMessage = msg)
    }
  }

  fun selectTier(tierId: String) {
    _uiState.update { it.copy(selectedTierId = tierId) }
  }

  fun toggleAiOptimization() {
    _uiState.update { state ->
      val newState = !state.isAiOptimized
      val msg = if (newState) {
        "AI Optimization applied: Switched to Vande Bharat & bundled pass, saving ₹3,500!"
      } else {
        "AI Optimization reverted."
      }
      state.copy(isAiOptimized = newState, feedbackMessage = msg)
    }
  }

  fun openAddActivityDialog() {
    _uiState.update { it.copy(showAddActivityDialog = true) }
  }

  fun closeAddActivityDialog() {
    _uiState.update { it.copy(showAddActivityDialog = false) }
  }

  fun addActivity(dayNumber: Int, title: String, time: String, cost: Int, desc: String) {
    _uiState.update { state ->
      val updatedDays = state.itineraryDays.map { day ->
        if (day.dayNumber == dayNumber) {
          val newActivity = ItineraryActivity(
            id = "custom_${System.currentTimeMillis()}",
            time = time.ifBlank { "04:30 PM" },
            title = title.ifBlank { "Custom Activity" },
            description = desc.ifBlank { "Explore local sights & experiences." },
            costTag = if (cost > 0) state.currency.format(cost) else "Free",
            costInr = cost,
            isFree = cost == 0,
            category = "activity"
          )
          day.copy(activities = day.activities + newActivity)
        } else {
          day
        }
      }
      state.copy(
        itineraryDays = updatedDays,
        showAddActivityDialog = false,
        feedbackMessage = "Added \"$title\" to Day $dayNumber!"
      )
    }
  }

  fun downloadPdf() {
    _uiState.update {
      it.copy(feedbackMessage = "Exported Jaipur & Udaipur Royal Itinerary PDF!")
    }
  }

  fun clearFeedbackMessage() {
    _uiState.update { it.copy(feedbackMessage = null) }
  }

  fun getDestination(id: String): Destination {
    return TripMateRepository.destinations.find { it.id == id }
      ?: TripMateRepository.destinations.first()
  }
}

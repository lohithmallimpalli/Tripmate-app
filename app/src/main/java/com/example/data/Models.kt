package com.example.data

data class Destination(
  val id: String,
  val title: String,
  val state: String,
  val region: String, // "North India", "South India", "West & Goa", "Himalayas", "Northeast & East"
  val category: String, // "goa", "ladakh", "rajasthan", "kerala", "himachal", "all"
  val badge: String,
  val seasonBadge: String,
  val rating: Double,
  val reviewCount: String,
  val priceInr: Int,
  val durationDays: Int,
  val imageUrl: String,
  val tags: List<String>,
  val description: String,
  val isFavorite: Boolean = false,
  val weather: String = "22°C / 72°F",
  val idealDuration: String = "3 – 5 Days",
  val subtitle: String = "The Spiritual Heart of India • Holy Kashi",
  val isSpiritual: Boolean = false,
  val isBackpacker: Boolean = true,
  val isFamily: Boolean = true,
)

data class BudgetTier(
  val id: String,
  val title: String,
  val description: String,
  val costPerDayInr: Int,
  val isPopular: Boolean = false,
  val iconType: String, // "backpack", "hotel", "spa"
)

data class Attraction(
  val id: String,
  val title: String,
  val tag: String,
  val timeInfo: String,
  val rating: Double,
  val reviewCount: String,
  val imageUrl: String,
  val description: String,
)

data class LocalDish(
  val id: String,
  val title: String,
  val location: String,
  val priceInr: Int,
  val priceSuffix: String = "ea", // "thali" or "ea"
  val rating: Double,
  val reviewCount: String,
  val recommendedSpot: String,
  val imageUrl: String,
  val description: String,
)

data class RecommendedStay(
  val id: String,
  val name: String,
  val location: String,
  val badge: String,
  val rating: Double,
  val ratingText: String,
  val reviewCount: String,
  val pricePerNightInr: Int,
  val imageUrl: String,
  val description: String,
)

data class Deal(
  val id: String,
  val title: String,
  val transportType: String, // "flight", "bus", "train"
  val transportCost: String,
  val roomRate: String,
  val totalEstimatedInr: Int,
  val imageUrl: String,
)

data class ItineraryActivity(
  val id: String,
  val time: String,
  val title: String,
  val description: String,
  val costTag: String,
  val costInr: Int = 0,
  val isFree: Boolean = false,
  val isReserved: Boolean = false,
  val imageUrl: String? = null,
  val category: String = "other", // "lodging", "transit", "food", "activity"
)

data class ItineraryDay(
  val dayNumber: Int,
  val dateString: String,
  val title: String,
  val locationArea: String,
  val activities: List<ItineraryActivity>,
)

enum class Currency(val code: String, val symbol: String, val rateFromInr: Double) {
  INR("INR", "₹", 1.0),
  USD("USD", "$", 0.012),
  EUR("EUR", "€", 0.011),
  GBP("GBP", "£", 0.0095);

  fun format(inrAmount: Number): String {
    val converted = Math.round(inrAmount.toDouble() * rateFromInr)
    return when (this) {
      INR -> {
        val str = converted.toString()
        if (str.length > 3) {
          val lastThree = str.substring(str.length - 3)
          val otherNumbers = str.substring(0, str.length - 3)
          val formattedOther = otherNumbers.reversed().chunked(2).joinToString(",").reversed()
          "₹$formattedOther,$lastThree"
        } else {
          "₹$str"
        }
      }
      USD -> "$$converted"
      EUR -> "€$converted"
      GBP -> "£$converted"
    }
  }
}

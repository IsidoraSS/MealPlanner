package rs.raf.mealPlanner.data.models.utils

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyPlan(
    var breakfastMealId: String?,
    var lunchMealId: String?,
    var dinnerMealId: String?,
    var snackMealId: String?
)

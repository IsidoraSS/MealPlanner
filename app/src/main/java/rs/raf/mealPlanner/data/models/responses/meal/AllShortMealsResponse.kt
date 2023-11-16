package rs.raf.mealPlanner.data.models.responses.meal

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllShortMealsResponse(
    val meals: List<ShortMealResponse>?
)

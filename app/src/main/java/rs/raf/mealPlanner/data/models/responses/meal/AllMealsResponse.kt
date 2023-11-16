package rs.raf.mealPlanner.data.models.responses.meal

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllMealsResponse (
    val meals: List<MealResponse>?
)
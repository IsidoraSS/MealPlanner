package rs.raf.mealPlanner.data.models.responses.meal

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShortMealResponse(
    val idMeal : String,
    val strMeal : String,
    val strMealThumb : String,
)

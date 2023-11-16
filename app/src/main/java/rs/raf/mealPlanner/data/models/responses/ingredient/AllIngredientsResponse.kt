package rs.raf.mealPlanner.data.models.responses.ingredient

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllIngredientsResponse(
    val meals: List<IngredientResponse>
)

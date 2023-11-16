package rs.raf.mealPlanner.data.models.responses.ingredient

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientResponse(
    val idIngredient : String,
    val strIngredient : String,
)

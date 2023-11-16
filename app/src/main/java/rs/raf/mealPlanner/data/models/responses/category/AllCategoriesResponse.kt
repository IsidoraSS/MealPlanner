package rs.raf.mealPlanner.data.models.responses.category

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllCategoriesResponse(
    val categories: List<CategoryResponse>
)
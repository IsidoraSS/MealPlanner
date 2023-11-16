package rs.raf.mealPlanner.data.models.responses.area

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AreaResponse(
    val strArea: String
)

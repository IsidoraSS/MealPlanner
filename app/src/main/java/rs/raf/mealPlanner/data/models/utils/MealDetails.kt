package rs.raf.mealPlanner.data.models.utils

data class MealDetails(
    var id : String,
    var name : String?,
    var category : String?,
    var area : String?,
    var recipe : String?,
    var image : String?,
    var youtube : String?,
    var tags : String?,
    var ingredients: Map<String,String>
)

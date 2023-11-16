package rs.raf.mealPlanner.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "saved_meals")
data class MealEntity(
    @PrimaryKey
    var id : String,
    var name : String?,
    var category : String?,
    var area : String?,
    var recipe : String?,
    var image : String?,
    var youtube : String?,
    var tags : String?,
    var ingredients: Map<String,String>,
    var date: Date?,
    var type: String?,
)

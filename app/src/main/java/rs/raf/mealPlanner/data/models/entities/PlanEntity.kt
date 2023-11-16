package rs.raf.mealPlanner.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import rs.raf.mealPlanner.data.models.utils.DailyPlan

@Entity(tableName = "plans")
data class PlanEntity(
    @PrimaryKey
    var name : String,
    var days: List<DailyPlan>
)

package rs.raf.mealPlanner.data.datasources.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rs.raf.mealPlanner.data.datasources.local.daos.MealDao
import rs.raf.mealPlanner.data.datasources.local.daos.PlanDao
import rs.raf.mealPlanner.data.datasources.local.db.converters.*
import rs.raf.mealPlanner.data.models.entities.MealEntity
import rs.raf.mealPlanner.data.models.entities.PlanEntity

@Database(
    entities = [MealEntity::class, PlanEntity::class],
    version = 7,
    exportSchema = false)
@TypeConverters(DateConverter::class,ListConverter::class,MapConverter::class, DailyPlanConverter::class)
abstract class MealDB : RoomDatabase() {
    abstract fun getMealDao(): MealDao
    abstract fun getPlanDao(): PlanDao
}
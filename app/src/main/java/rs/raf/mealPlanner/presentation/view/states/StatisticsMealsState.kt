package rs.raf.mealPlanner.presentation.view.states

import rs.raf.mealPlanner.data.models.utils.MealDetails

sealed class StatisticsMealsState{
    data class Success(val fullMeals: List<MealDetails>): StatisticsMealsState()
    data class Error(val message: String): StatisticsMealsState()
    object Loading: StatisticsMealsState()
    object DataFetched: StatisticsMealsState()
}

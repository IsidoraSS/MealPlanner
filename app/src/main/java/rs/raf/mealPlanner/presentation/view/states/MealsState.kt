package rs.raf.mealPlanner.presentation.view.states

import rs.raf.mealPlanner.data.models.utils.Meal


sealed class MealsState{
    object Loading: MealsState()
    object DataFetched: MealsState()
    data class Success(val meals: List<Meal>): MealsState()
    data class Error(val message: String): MealsState()
}

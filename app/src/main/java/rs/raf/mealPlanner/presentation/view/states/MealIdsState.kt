package rs.raf.mealPlanner.presentation.view.states

sealed class MealIdsState{
    data class Success(val mealIds: List<String>): MealIdsState()
    data class Error(val message: String): MealIdsState()
}


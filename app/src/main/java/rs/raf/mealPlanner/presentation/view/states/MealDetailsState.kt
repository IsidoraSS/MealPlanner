package rs.raf.mealPlanner.presentation.view.states

import rs.raf.mealPlanner.data.models.utils.MealDetails

sealed class MealDetailsState{
    object Loading: MealDetailsState()
    object DataFetched: MealDetailsState()
    data class Success(val mealDetails: MealDetails): MealDetailsState()
    data class Error(val message: String): MealDetailsState()
}

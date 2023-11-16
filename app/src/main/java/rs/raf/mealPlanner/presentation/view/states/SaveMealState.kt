package rs.raf.mealPlanner.presentation.view.states

sealed class SaveMealState{
    object Success: SaveMealState()
    data class Error(val message: String): SaveMealState()
}

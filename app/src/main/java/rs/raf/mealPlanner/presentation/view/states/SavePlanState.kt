package rs.raf.mealPlanner.presentation.view.states

sealed class SavePlanState {
    object Success: SavePlanState()
    data class Error(val message: String): SavePlanState()
}
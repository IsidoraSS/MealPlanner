package rs.raf.mealPlanner.presentation.view.states

sealed class AddMovieState {
    object Success: AddMovieState()
    data class Error(val message: String): AddMovieState()
}
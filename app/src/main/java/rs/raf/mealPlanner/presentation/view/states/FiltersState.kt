package rs.raf.mealPlanner.presentation.view.states

import rs.raf.mealPlanner.data.models.utils.Filter

sealed class FiltersState{
    object Loading: FiltersState()
    object DataFetched: FiltersState()
    data class Success(val filters: List<Filter>): FiltersState()
    data class Error(val message: String): FiltersState()
}

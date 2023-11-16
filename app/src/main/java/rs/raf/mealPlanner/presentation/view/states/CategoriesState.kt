package rs.raf.mealPlanner.presentation.view.states

import rs.raf.mealPlanner.data.models.utils.Category

sealed class CategoriesState{
    object Loading: CategoriesState()
    object DataFetched: CategoriesState()
    data class Success(val categories: List<Category>): CategoriesState()
    data class Error(val message: String): CategoriesState()
}

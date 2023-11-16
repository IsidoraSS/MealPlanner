package rs.raf.mealPlanner.data.repositories

import io.reactivex.Observable
import rs.raf.mealPlanner.data.models.utils.Resource
import rs.raf.mealPlanner.data.models.utils.Category
import rs.raf.mealPlanner.data.models.utils.Filter

interface FiltersRepository {
    fun fetchAllCategories(): Observable<Resource<List<Category>>>

    fun fetchAllCategoryNames(): Observable<Resource<List<Filter>>>
    fun fetchAllAreaNames(): Observable<Resource<List<Filter>>>
    fun fetchAllIngredientNames(): Observable<Resource<List<Filter>>>
}
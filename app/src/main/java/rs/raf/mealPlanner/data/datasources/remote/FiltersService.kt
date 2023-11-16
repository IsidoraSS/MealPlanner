package rs.raf.mealPlanner.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import rs.raf.mealPlanner.data.models.responses.area.AllAreasResponse
import rs.raf.mealPlanner.data.models.responses.category.AllCategoriesResponse
import rs.raf.mealPlanner.data.models.responses.category.AllShortCategoriesResponse
import rs.raf.mealPlanner.data.models.responses.ingredient.AllIngredientsResponse

interface FiltersService {
    @GET("categories.php")
    fun fetchAllCategories(): Observable<AllCategoriesResponse>


    //filteri
    @GET("list.php?c=list")
    fun fetchAllCategoryNames(): Observable<AllShortCategoriesResponse>

    @GET("list.php?a=list")
    fun fetchAllAreaNames(): Observable<AllAreasResponse>

    @GET("list.php?i=list")
    fun fetchAllIngredientNames(): Observable<AllIngredientsResponse>
}
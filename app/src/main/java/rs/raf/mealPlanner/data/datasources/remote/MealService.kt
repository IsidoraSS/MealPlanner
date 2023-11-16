package rs.raf.mealPlanner.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.mealPlanner.data.models.responses.meal.AllMealsResponse
import rs.raf.mealPlanner.data.models.responses.meal.AllShortMealsResponse

interface MealService {

    @GET("filter.php")
    fun fetchByCategory(@Query("c") category:String): Observable<AllShortMealsResponse>

    @GET("filter.php")
    fun fetchByArea(@Query("a") area:String): Observable<AllShortMealsResponse>

    @GET("filter.php")
    fun fetchByIngredient(@Query("i") ingredient:String): Observable<AllShortMealsResponse>

    @GET("lookup.php")
    fun fetchMealDetails(@Query("i") mealId:String): Observable<AllMealsResponse>
}
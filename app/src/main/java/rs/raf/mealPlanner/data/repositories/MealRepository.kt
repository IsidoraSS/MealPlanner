package rs.raf.mealPlanner.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.mealPlanner.data.models.entities.MealEntity
import rs.raf.mealPlanner.data.models.utils.DateCount
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.data.models.utils.MealDetails
import rs.raf.mealPlanner.data.models.utils.Resource

interface MealRepository {
    fun fetchByCategory(category:String): Observable<Resource<List<Meal>>>
    fun fetchByArea(area:String): Observable<Resource<List<Meal>>>
    fun fetchByIngredient(ingredient:String): Observable<Resource<List<Meal>>>

    fun fetchMealDetails(id:String): Observable<Resource<MealDetails>>

    fun saveMealToDB(meal:MealEntity): Completable
    fun deleteMealById(id:String): Completable

    fun getMealCountByLast7Days(): Observable<List<DateCount>>

    fun getListOfMealsFromIds(idList: List<String>): Observable<Resource<List<MealDetails>>>
}
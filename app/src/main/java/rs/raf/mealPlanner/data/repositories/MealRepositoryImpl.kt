package rs.raf.mealPlanner.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.mealPlanner.data.datasources.local.daos.MealDao
import rs.raf.mealPlanner.data.datasources.remote.MealService
import rs.raf.mealPlanner.data.models.entities.MealEntity
import rs.raf.mealPlanner.data.models.utils.Resource
import rs.raf.mealPlanner.data.models.responses.meal.MealResponse
import rs.raf.mealPlanner.data.models.utils.DateCount
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.data.models.utils.MealDetails
import kotlin.reflect.full.declaredMemberProperties

class MealRepositoryImpl(
    private val remoteMealSource: MealService,
    private val localMealSource: MealDao
) : MealRepository  {

    override fun fetchByCategory(category: String): Observable<Resource<List<Meal>>> {
        return remoteMealSource
            .fetchByCategory(category)
            .flatMap { allMealsResponse->
                val meals= allMealsResponse.meals!!.map{
                    Meal(
                        it.idMeal,
                        it.strMeal,
                        it.strMealThumb
                    )
                }
                Observable.just(Resource.Success(meals))
            }
    }

    override fun fetchByArea(area: String): Observable<Resource<List<Meal>>> {
        return remoteMealSource
            .fetchByArea(area)
            .flatMap { allMealsResponse->
                val meals= allMealsResponse.meals!!.map{
                    Meal(
                        it.idMeal,
                        it.strMeal,
                        it.strMealThumb
                    )
                }
                Observable.just(Resource.Success(meals))
            }
    }

    override fun fetchByIngredient(ingredient: String): Observable<Resource<List<Meal>>> {
        return remoteMealSource
            .fetchByIngredient(ingredient)
            .flatMap { allMealsResponse->
                val meals= allMealsResponse.meals!!.map{
                    Meal(
                        it.idMeal,
                        it.strMeal,
                        it.strMealThumb
                    )
                }
                Observable.just(Resource.Success(meals))
            }
    }

    override fun fetchMealDetails(id: String): Observable<Resource<MealDetails>> {
        return remoteMealSource
            .fetchMealDetails(id)
            .flatMap { mealDetails->
                val meal= mealDetails.meals!!.get(0)
                val ingredientsMap = mutableMapOf<String, String>()

                for (i in 1..20) {
                    val ingredient = getIngredient(meal,i)
                    val measure = getMeasure(meal,i)
                    if (!ingredient.isNullOrBlank()) {
                        ingredientsMap[ingredient] = measure ?: ""
                    }
                }

                val finished = MealDetails(
                    meal.idMeal,
                    meal.strMeal,
                    meal.strCategory,
                    meal.strArea,
                    meal.strInstructions,
                    meal.strMealThumb,
                    meal.strYoutube,
                    meal.strTags,
                    ingredientsMap
                )
                Observable.just(Resource.Success(finished))
            }
    }

    override fun saveMealToDB(meal: MealEntity): Completable {
        return localMealSource.insert(meal)
    }

    override fun deleteMealById(id: String): Completable {
        return localMealSource.deleteMealById(id)
    }

    override fun getMealCountByLast7Days(): Observable<List<DateCount>> {
        return localMealSource.getMealCountByLast7Days()
    }

//    override fun getListOfMealsFromIds(idList: List<String>): Observable<List<MealDetails>> {
//        val observables = idList.map { mealId ->
//            remoteMealSource.fetchMealDetails(mealId)
//        }
//
//        return Observable.concat(observables)
//            .map { it }
//            .map { response ->
//                val meal = response.meals!!.get(0)
//                val ingredientsMap = mutableMapOf<String, String>()
//
//                for (i in 1..20) {
//                    val ingredient = getIngredient(meal, i)
//                    val measure = getMeasure(meal, i)
//                    if (!ingredient.isNullOrBlank()) {
//                        ingredientsMap[ingredient] = measure ?: ""
//                    }
//                }
//
//                MealDetails(
//                    meal.idMeal,
//                    meal.strMeal,
//                    meal.strCategory,
//                    meal.strArea,
//                    meal.strInstructions,
//                    meal.strMealThumb,
//                    meal.strYoutube,
//                    meal.strTags,
//                    ingredientsMap
//                )
//            }
//            .toList()
//            .toObservable()
//    }

    override fun getListOfMealsFromIds(idList: List<String>): Observable<Resource<List<MealDetails>>> {
        val observables = idList.map { mealId ->
            remoteMealSource.fetchMealDetails(mealId)
        }

        val list = Observable.concat(observables)
            .map { it }
            .map { response ->
                val meal = response.meals!!.get(0)
                val ingredientsMap = mutableMapOf<String, String>()

                for (i in 1..20) {
                    val ingredient = getIngredient(meal, i)
                    val measure = getMeasure(meal, i)
                    if (!ingredient.isNullOrBlank()) {
                        ingredientsMap[ingredient] = measure ?: ""
                    }
                }

                MealDetails(
                    meal.idMeal,
                    meal.strMeal,
                    meal.strCategory,
                    meal.strArea,
                    meal.strInstructions,
                    meal.strMealThumb,
                    meal.strYoutube,
                    meal.strTags,
                    ingredientsMap
                )
            }
            .toList()
            .toObservable() // Convert the Single to Observable

        return list.map { Resource.Success(it) }
    }



    //    override fun getListOfMealsFromIds(idList: List<String>): Observable<List<MealDetails>> {
//        val list = mutableListOf<MealDetails>()
//        idList.map{
//            remoteMealSource.fetchMealDetails(it)
//                .flatMap { mealDetails->
//                    val meal= mealDetails.meals!!.get(0)
//                    val ingredientsMap = mutableMapOf<String, String>()
//
//                    for (i in 1..20) {
//                        val ingredient = getIngredient(meal,i)
//                        val measure = getMeasure(meal,i)
//                        if (!ingredient.isNullOrBlank()) {
//                            ingredientsMap[ingredient] = measure ?: ""
//                        }
//                    }
//
//                    val finished = MealDetails(
//                        meal.idMeal,
//                        meal.strMeal,
//                        meal.strCategory,
//                        meal.strArea,
//                        meal.strInstructions,
//                        meal.strMealThumb,
//                        meal.strYoutube,
//                        meal.strTags,
//                        ingredientsMap
//                    )
//                    list.add(finished)
//                    Observable.just(Resource.Success(finished)) }
//        }
//        return Observable.just(list.toList())
//    }

    //          ********OVAJ KOD POLU RADI********
//    override fun fetchAll(): Observable<Resource<Unit>> {
//        val mealList = mutableListOf<MealApiEntity>()
//        val test = localCategorySource
//            .getAll()
//            .flatMapIterable { it }
//            .flatMap { category->
//                remoteMealSource.fetchAllByCategory(category.name)
//            }
//            .flatMapIterable {
//                it.meals
//            }
//            .flatMap {
//                remoteMealSource.fetchMealDetails(it.idMeal)
//            }
//            .flatMap<Resource<Unit>?> { allMealsResponse->
//                val mealEntities=allMealsResponse.meals?.map{
//                    val ingredientsMap = mutableMapOf<String, String>()
//                    // Loop through ingredients and measures, adding non-empty ones to the map
//                    for (i in 1..20) {
//                        val ingredient = it.getIngredient(i)
//                        val measure = it.getMeasure(i)
//
//                        // Skip if ingredient is null or empty
//                        if (!ingredient.isNullOrBlank()) {
//                            ingredientsMap[ingredient] = measure ?: ""
//                        }
//                    }
//
//                    MealApiEntity(
//                        it.idMeal,
//                        it.strMeal,
//                        it.strCategory,
//                        it.strArea,
//                        it.strInstructions,
//                        it.strMealThumb,
//                        it.strYoutube,
//                        it.strTags,
//                        ingredientsMap
//                    )
//                } ?: emptyList()
//
//                mealList.addAll(mealEntities)
//                Observable.just(Resource.Success(Unit))
//            }
//            localMealSource.insertAll(mealList)
//            return test
//    }

    private fun getIngredient(meal: MealResponse, index: Int): String?{
        val reflection = meal.javaClass.kotlin
        reflection.declaredMemberProperties.forEach {
            if(it.name=="strIngredient$index"){
                return it.get(meal) as String?
            }
        }
        return null
    }

    private fun getMeasure(meal: MealResponse, index: Int): String?{
        val reflection = meal.javaClass.kotlin
        reflection.declaredMemberProperties.forEach {
            if(it.name=="strMeasure$index"){
                return it.get(meal) as String?
            }
        }
        return null
    }

}
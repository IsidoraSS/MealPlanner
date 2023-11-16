package rs.raf.mealPlanner.data.repositories

import io.reactivex.Observable
import rs.raf.mealPlanner.data.datasources.remote.FiltersService
import rs.raf.mealPlanner.data.models.utils.Resource
import rs.raf.mealPlanner.data.models.utils.Category
import rs.raf.mealPlanner.data.models.utils.Filter

class FiltersRepositoryImpl(
    private val remoteDataSource: FiltersService
) : FiltersRepository  {

    override fun fetchAllCategories(): Observable<Resource<List<Category>>> {
        return remoteDataSource
            .fetchAllCategories()
            .flatMap { allCategoriesResponse ->
                val categoryEntities = allCategoriesResponse.categories.map {
                    Category(
                        it.idCategory,
                        it.strCategory,
                        it.strCategoryThumb,
                        it.strCategoryDescription
                    )
                }
                Observable.just(Resource.Success(categoryEntities))
            }
    }

    override fun fetchAllCategoryNames(): Observable<Resource<List<Filter>>> {
        return remoteDataSource
            .fetchAllCategoryNames()
            .flatMap { allCategoryNames ->
                val categoryNames = allCategoryNames.meals.map {
                    Filter(
                        it.strCategory
                    )
                }
                Observable.just(Resource.Success(categoryNames))
            }
    }

    override fun fetchAllAreaNames(): Observable<Resource<List<Filter>>> {
        return remoteDataSource
            .fetchAllAreaNames()
            .flatMap { allAreaNames ->
                val areaNames = allAreaNames.meals.map {
                    Filter(
                        it.strArea
                    )
                }
                Observable.just(Resource.Success(areaNames))
            }
    }

    override fun fetchAllIngredientNames(): Observable<Resource<List<Filter>>> {
        return remoteDataSource
            .fetchAllIngredientNames()
            .flatMap { allIngredientNames ->
                val ingredientNames = allIngredientNames.meals.map {
                    Filter(
                        it.strIngredient
                    )
                }
                Observable.just(Resource.Success(ingredientNames))
            }
    }
}
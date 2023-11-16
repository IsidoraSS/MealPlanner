package rs.raf.mealPlanner.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.mealPlanner.data.models.entities.MealEntity
import rs.raf.mealPlanner.data.models.entities.PlanEntity
import rs.raf.mealPlanner.data.models.utils.*
import rs.raf.mealPlanner.data.repositories.FiltersRepository
import rs.raf.mealPlanner.data.repositories.MealRepository
import rs.raf.mealPlanner.data.repositories.PlanRepository
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.states.*
import timber.log.Timber

class MainViewModel(
    private val mealRepository: MealRepository, private val filtersRepository: FiltersRepository, private val planRepository: PlanRepository
) : ViewModel(), MainContract.ViewModel {

    private val subscriptions = CompositeDisposable()

    override val categoriesState: MutableLiveData<CategoriesState> = MutableLiveData()
    override val filtersState: MutableLiveData<FiltersState> = MutableLiveData()
    override val mealsState: MutableLiveData<MealsState> = MutableLiveData()
    //override val addDone: MutableLiveData<AddMovieState> = MutableLiveData()

    override val selectedCategory: MutableLiveData<Category> = MutableLiveData()
    override val selectedFilter: MutableLiveData<Filter> = MutableLiveData()
    override val selectedMeal: MutableLiveData<Meal> = MutableLiveData()

    override val selectedMealDetails: MutableLiveData<MealDetailsState> = MutableLiveData()

    override val mealSaved: MutableLiveData<SaveMealState> = MutableLiveData()

    override val weeklyCount: MutableLiveData<DateCountState> = MutableLiveData()

    override val planDialogSelectedFilter: MutableLiveData<Filter> = MutableLiveData()
    override val planDialogSelectedMeal: MutableLiveData<Meal> = MutableLiveData()

    override val planSaved: MutableLiveData<SavePlanState> = MutableLiveData()

    override val mealIdsState: MutableLiveData<MealIdsState> = MutableLiveData()
    override val statisticsMealDetails: MutableLiveData<StatisticsMealsState> = MutableLiveData()


    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
//        val subscription = publishSubject
//            .debounce(200, TimeUnit.MILLISECONDS)
//            .distinctUntilChanged()
//            .switchMap {
//                mealApiRepository
//                    .getAllByName(it)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnError {
//                        Timber.e("Error in publish subject")
//                        Timber.e(it)
//                    }
//            }
//            .subscribe(
//                {
//                    mealsState.value = MealsState.Success(it)
//                },
//                {
//                    mealsState.value = MealsState.Error("Error happened while fetching data from db")
//                    Timber.e(it)
//                }
//            )
//        subscriptions.add(subscription)
    }


    override fun fetchCategories() {
        val subscription = filtersRepository
            .fetchAllCategories()
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> categoriesState.value = CategoriesState.Loading
                        is Resource.Success -> categoriesState.value = CategoriesState.Success(it.data)
                        is Resource.Error -> categoriesState.value = CategoriesState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    categoriesState.value = CategoriesState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchCategoryNames() {
        val subscription = filtersRepository
            .fetchAllCategoryNames()
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> filtersState.value = FiltersState.Loading
                        is Resource.Success -> filtersState.value = FiltersState.Success(it.data)
                        is Resource.Error -> filtersState.value = FiltersState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    filtersState.value = FiltersState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAreaNames() {
        val subscription = filtersRepository
            .fetchAllAreaNames()
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> filtersState.value = FiltersState.Loading
                        is Resource.Success -> filtersState.value = FiltersState.Success(it.data)
                        is Resource.Error -> filtersState.value = FiltersState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    filtersState.value = FiltersState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchIngredientNames() {
        val subscription = filtersRepository
            .fetchAllIngredientNames()
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> filtersState.value = FiltersState.Loading
                        is Resource.Success -> filtersState.value = FiltersState.Success(it.data)
                        is Resource.Error -> filtersState.value = FiltersState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    filtersState.value = FiltersState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchMealsByCategory(category: String) {
        val subscription = mealRepository
            .fetchByCategory(category)
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> mealsState.value = MealsState.Loading
                        is Resource.Success -> mealsState.value = MealsState.Success(it.data)
                        is Resource.Error -> mealsState.value = MealsState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealsState.value = MealsState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchMealsByArea(area: String) {
        val subscription = mealRepository
            .fetchByArea(area)
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> mealsState.value = MealsState.Loading
                        is Resource.Success -> mealsState.value = MealsState.Success(it.data)
                        is Resource.Error -> mealsState.value = MealsState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealsState.value = MealsState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchMealsByIngredient(ingredient: String) {
        val subscription = mealRepository
            .fetchByIngredient(ingredient)
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> mealsState.value = MealsState.Loading
                        is Resource.Success -> mealsState.value = MealsState.Success(it.data)
                        is Resource.Error -> mealsState.value = MealsState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealsState.value = MealsState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchMealDetails(meal: Meal) {
        val subscription = mealRepository
            .fetchMealDetails(meal.id)
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> selectedMealDetails.value = MealDetailsState.Loading
                        is Resource.Success -> selectedMealDetails.value = MealDetailsState.Success(it.data)
                        is Resource.Error -> selectedMealDetails.value = MealDetailsState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealsState.value = MealsState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun setSelectedCategory(category: Category) {
        selectedCategory.value=category
    }

    override fun setSelectedFilter(filter: Filter) {
        selectedFilter.value=filter
    }

    override fun setSelectedMeal(meal: Meal) {
        selectedMeal.value=meal
    }

    override fun saveMeal(meal: MealEntity) {
        val subscription = mealRepository
            .saveMealToDB(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealSaved.value = SaveMealState.Success
                },
                {
                    mealSaved.value = SaveMealState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getWeeklyCount() {
        val subscription = mealRepository
            .getMealCountByLast7Days()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    weeklyCount.value = DateCountState.Success(it)
                    Timber.e(it.toString())
                },
                {
                    weeklyCount.value = DateCountState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun setplanDialogSelectedFilter(filter: Filter) {
        planDialogSelectedFilter.value=filter
    }

    override fun setPlanDialogSelectedMeal(meal: Meal) {
        planDialogSelectedMeal.value=meal
    }

    override fun savePlan(plan: PlanEntity) {
        val subscription = planRepository
            .savePlanToDB(plan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    planSaved.value = SavePlanState.Success
                },
                {
                    planSaved.value = SavePlanState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getMealIdsState() {
        val subscription = planRepository
            .getAllMealIdsFromAllPlans()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealIdsState.value = MealIdsState.Success(it)
                },
                {
                    mealIdsState.value = MealIdsState.Error("Error happened while adding movie")
                }
            )
        subscriptions.add(subscription)
    }

    override fun getStatisticMealsDetails(ids: List<String>) {
        val subscription = mealRepository
            .getListOfMealsFromIds(ids)
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> statisticsMealDetails.value = StatisticsMealsState.Loading
                        is Resource.Success -> statisticsMealDetails.value = StatisticsMealsState.Success(it.data)
                        is Resource.Error -> statisticsMealDetails.value = StatisticsMealsState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    statisticsMealDetails.value = StatisticsMealsState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}
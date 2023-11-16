package rs.raf.mealPlanner.presentation.contract

import androidx.lifecycle.LiveData
import rs.raf.mealPlanner.data.models.entities.MealEntity
import rs.raf.mealPlanner.data.models.entities.PlanEntity
import rs.raf.mealPlanner.data.models.utils.Category
import rs.raf.mealPlanner.data.models.utils.Filter
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.presentation.view.states.*

interface MainContract {

    interface ViewModel {

        val mealsState: LiveData<MealsState>
        val categoriesState: LiveData<CategoriesState>
        val filtersState: LiveData<FiltersState>
//        val addDone: LiveData<AddMovieState>
        val selectedCategory: LiveData<Category>
        val selectedFilter: LiveData<Filter>
        val selectedMeal: LiveData<Meal>

        val selectedMealDetails: LiveData<MealDetailsState>
        val mealSaved: LiveData<SaveMealState>

        val weeklyCount: LiveData<DateCountState>

        val planDialogSelectedFilter: LiveData<Filter>
        val planDialogSelectedMeal: LiveData<Meal>

        val planSaved: LiveData<SavePlanState>

        val mealIdsState: LiveData<MealIdsState>
        val statisticsMealDetails: LiveData<StatisticsMealsState>

        fun fetchCategories()

        fun fetchCategoryNames()
        fun fetchAreaNames()
        fun fetchIngredientNames()

        fun setSelectedCategory(category: Category)
        fun setSelectedFilter(filter: Filter)
        fun setSelectedMeal(meal:Meal)

        fun fetchMealsByCategory(category: String)
        fun fetchMealsByArea(area: String)
        fun fetchMealsByIngredient(ingredient: String)

        fun fetchMealDetails(meal:Meal)
        fun saveMeal(meal: MealEntity)

        fun getWeeklyCount()

        fun setplanDialogSelectedFilter(filter:Filter)
        fun setPlanDialogSelectedMeal(meal:Meal)

        fun savePlan(plan:PlanEntity)

        fun getMealIdsState()
        fun getStatisticMealsDetails(ids: List<String>)
    }

}
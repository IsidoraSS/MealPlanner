package rs.raf.mealPlanner.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.mealPlanner.data.datasources.local.db.MealDB
import rs.raf.mealPlanner.data.datasources.remote.FiltersService
import rs.raf.mealPlanner.data.datasources.remote.MealService
import rs.raf.mealPlanner.data.repositories.*
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel

val movieModule = module {

    viewModel { MainViewModel(mealRepository = get(), filtersRepository = get(), planRepository = get())}

    single<FiltersRepository> { FiltersRepositoryImpl(remoteDataSource = get()) }
    single<MealRepository> { MealRepositoryImpl(remoteMealSource = get(), localMealSource = get()) }

    single<PlanRepository> { PlanRepositoryImpl(localPlanSource = get()) }

    single<MealService> { create(retrofit = get()) }

    single { get<MealDB>().getMealDao() }
    single { get<MealDB>().getPlanDao() }

    single<FiltersService> { create(retrofit = get()) }
}


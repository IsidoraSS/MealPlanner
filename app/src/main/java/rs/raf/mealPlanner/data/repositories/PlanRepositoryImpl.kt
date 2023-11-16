package rs.raf.mealPlanner.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.mealPlanner.data.datasources.local.daos.PlanDao
import rs.raf.mealPlanner.data.models.entities.PlanEntity

class PlanRepositoryImpl(private val localPlanSource: PlanDao): PlanRepository {

    override fun savePlanToDB(plan: PlanEntity): Completable {
        return localPlanSource.insert(plan)
    }

    override fun getAllMealIdsFromAllPlans(): Observable<List<String>>{
        return localPlanSource.getAll()
            .flatMap { allPlans ->
                Observable.fromCallable {
                    val mealIds = mutableListOf<String>()

                    allPlans.forEach { planEntity ->
                        planEntity.days.forEach { dailyPlan ->
                            dailyPlan.apply {
                                breakfastMealId?.let { mealIds.add(it) }
                                lunchMealId?.let { mealIds.add(it) }
                                dinnerMealId?.let { mealIds.add(it) }
                                snackMealId?.let { mealIds.add(it) }
                            }
                        }
                    }
                    mealIds
                }
            }
    }

}
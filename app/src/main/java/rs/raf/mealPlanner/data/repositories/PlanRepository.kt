package rs.raf.mealPlanner.data.repositories
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.mealPlanner.data.models.entities.PlanEntity

interface PlanRepository {
    fun savePlanToDB(plan: PlanEntity): Completable

    fun getAllMealIdsFromAllPlans(): Observable<List<String>>
}
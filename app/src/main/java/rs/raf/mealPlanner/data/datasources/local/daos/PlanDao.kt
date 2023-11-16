package rs.raf.mealPlanner.data.datasources.local.daos

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.mealPlanner.data.models.entities.PlanEntity

@Dao
abstract class PlanDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: PlanEntity): Completable

    @Query("SELECT * FROM plans")
    abstract fun getAll(): Observable<List<PlanEntity>>
}
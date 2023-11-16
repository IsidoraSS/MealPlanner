package rs.raf.mealPlanner.data.datasources.local.daos

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.mealPlanner.data.models.entities.MealEntity
import rs.raf.mealPlanner.data.models.utils.DateCount

@Dao
abstract class MealDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: MealEntity): Completable

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insertAll(entities: List<MealEntity>): Completable

    @Query("SELECT * FROM saved_meals")
    abstract fun getAll(): Observable<List<MealEntity>>

    @Query("DELETE FROM saved_meals")
    abstract fun deleteAll()

    @Query("DELETE FROM saved_meals WHERE id")

    @Transaction
    open fun deleteAndInsertAll(entities: List<MealEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }

    @Query("DELETE FROM saved_meals WHERE id = :mealId")
    abstract fun deleteMealById(mealId:String): Completable

    @Query("SELECT * FROM saved_meals WHERE name LIKE :name || '%'")
    abstract fun getByName(name: String): Observable<List<MealEntity>>

    @Query("SELECT * FROM saved_meals WHERE category LIKE :category || '%'")
    abstract fun getByCategory(category: String): Observable<List<MealEntity>>

    @Query("SELECT * FROM saved_meals WHERE area LIKE :area || '%'")
    abstract fun getByArea(area: String): Observable<List<MealEntity>>

    @Query("SELECT * FROM saved_meals WHERE ingredients LIKE :ingredient || '%'")
    abstract fun getByIngredient(ingredient: String): Observable<List<MealEntity>>

    @Query("SELECT (7 - (julianday('now') - julianday(datetime(datetime(date / 1000, 'unixepoch')))) + 1) AS dateNumber, COUNT(*) AS count FROM saved_meals WHERE date >= strftime('%s', 'now', '-7 days') GROUP BY dateNumber ORDER BY dateNumber DESC")
    abstract fun getMealCountByLast7Days(): Observable<List<DateCount>>
}
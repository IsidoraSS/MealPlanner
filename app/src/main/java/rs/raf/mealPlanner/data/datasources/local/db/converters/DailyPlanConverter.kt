package rs.raf.mealPlanner.data.datasources.local.db.converters
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.core.KoinComponent
import org.koin.core.get
import rs.raf.mealPlanner.data.models.utils.DailyPlan
import java.lang.reflect.Type

class DailyPlanConverter:KoinComponent {
    private val jsonAdapter: JsonAdapter<List<DailyPlan>>

    init {
        val type: Type = Types.newParameterizedType(List::class.java, DailyPlan::class.java)
        val moshi: Moshi = get()
        jsonAdapter = moshi.adapter(type)
    }

    @TypeConverter
    fun listMyModelToJsonStr(listMyModel: List<DailyPlan>?): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListMyModel(jsonStr: String?): List<DailyPlan>? {
        return jsonStr?.let { jsonAdapter.fromJson(jsonStr) }
    }
}
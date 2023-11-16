package rs.raf.mealPlanner.data.datasources.local.db.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.core.KoinComponent
import rs.raf.mealPlanner.data.models.utils.DailyPlan
import java.lang.reflect.ParameterizedType

class DPConverter: KoinComponent {
    private val moshi = Moshi.Builder().build()
    private val listMyData : ParameterizedType = Types.newParameterizedType(DailyPlan::class.java)
    private val jsonAdapter: JsonAdapter<DailyPlan> = moshi.adapter(listMyData)

    @TypeConverter
    fun listMyModelToJsonStr(listMyModel: DailyPlan?): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListMyModel(jsonStr: String?): DailyPlan? {
        return jsonStr?.let { jsonAdapter.fromJson(jsonStr) }
    }
}
package rs.raf.mealPlanner.data.datasources.local.db.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.core.KoinComponent
import org.koin.core.get
import java.lang.reflect.Type

class MapConverter: KoinComponent {
    private val jsonAdapter: JsonAdapter<Map<String, String>>

    init {
        val type: Type = Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
        val moshi: Moshi = get()
        jsonAdapter = moshi.adapter(type)
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return jsonAdapter.toJson(map)
    }

    @TypeConverter
    fun toMap(data: String): Map<String, String> {
        return jsonAdapter.fromJson(data).orEmpty()
    }
}
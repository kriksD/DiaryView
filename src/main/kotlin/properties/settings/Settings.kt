package properties.settings

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Settings(
    var dummyValue: String = ""
) {
    companion object {
        fun createFromJson(jsonObject: JsonObject): Settings? {
            try {
                val map = jsonObject.toMap()
                val newSettings = Settings()
                map["dummyValue"]?.jsonPrimitive?.contentOrNull?.let { newSettings.dummyValue = it }

                return newSettings
            } catch (e: Exception) { return null }
        }
    }
}
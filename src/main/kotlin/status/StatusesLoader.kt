package status

import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uniqueId
import java.io.File

class StatusesLoader {
    private val statusesList = mutableStateListOf<StatusEntry>()
    val statuses get() = statusesList.toList()

    private val folder = File("data/statuses")
    private val file = File(folder, "statuses.jsonl")

    fun load() {
        if (!file.exists()) return
        val jsonLines = file.readText()
        jsonLines.split("\n").forEach { line ->
            statusesList.add(Json.decodeFromString(line))
        }
    }

    fun save() {
        folder.mkdirs()

        var jsonLines = ""
        statusesList.forEach { status ->
            jsonLines += Json.encodeToString(status) + "\n"
        }
        jsonLines = jsonLines.dropLast(1)

        file.writeText(jsonLines)
    }

    fun addStatus(content: String) {
        val status = StatusEntry(statusesList.uniqueId(), content)
        statusesList.add(status)
    }

    fun removeStatus(status: StatusEntry) {
        statusesList.remove(status)
    }
}
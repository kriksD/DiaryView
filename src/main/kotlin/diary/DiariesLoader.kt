package diary

import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uniqueId
import java.io.File

class DiariesLoader {
    private val diariesList = mutableStateListOf<Diary>()
    val diaries get() = diariesList.toList()

    private val folder = File("data/diaries")

    fun load() {
        val files = folder.listFiles()?.filter { it.extension == "json" } ?: return
        files.forEach { file ->
            try {
                diariesList.add(Json.decodeFromString(file.readText()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun save() {
        folder.mkdirs()
        diariesList.forEach { diary ->
            val file = File(folder, "diary_id${diary.id}.json")
            file.writeText(Json.encodeToString(diary))
        }
    }

    fun addDiary(title: String, description: String) {
        val diary = Diary(diariesList.uniqueId(), title, description)
        diariesList.add(diary)
    }

    fun removeDiary(diary: Diary) {
        diariesList.remove(diary)
    }
}
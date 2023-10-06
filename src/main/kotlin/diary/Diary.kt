package diary

import HasId
import kotlinx.serialization.Serializable
import uniqueId

@Serializable(DiarySerializer::class)
class Diary(
    override val id: Int,
    var title: String,
    var description: String,
) : HasId {
    private val diaryEntries = mutableListOf<DiaryEntry>()

    fun createEntry(title: String): DiaryEntry {
        val newEntry = DiaryEntry(diaryEntries.uniqueId(), title)
        diaryEntries.add(newEntry)
        return newEntry
    }

    fun addEntry(entry: DiaryEntry) {
        if (diaryEntries.any { it.id == entry.id }) {
            diaryEntries.add(
                DiaryEntry(
                    id = diaryEntries.uniqueId(),
                    title = entry.title,
                    content = entry.content,
                    dateCreate = entry.dateCreate,
                    dateEdit = entry.dateEdit
                )
            )
        } else {
            diaryEntries.add(entry)
        }
    }

    fun removeEntry(id: Int) {
        diaryEntries.removeAt(id)
    }

    fun removeEntry(entry: DiaryEntry) {
        diaryEntries.remove(entry)
    }

    val entries: List<DiaryEntry> get() = diaryEntries
}
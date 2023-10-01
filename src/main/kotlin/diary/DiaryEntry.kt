package diary

import kotlinx.serialization.Serializable

@Serializable(DiaryEntrySerializer::class)
class DiaryEntry(
    val id: Int,
    title: String,
    content: String = "",
    val dateCreate: Long = System.currentTimeMillis(),
    dateEdit: Long = System.currentTimeMillis(),
) {
    var title: String = title
        set(value) {
            field = value
            dateEdit = System.currentTimeMillis()
        }

    var content: String = content
        set(value) {
            field = value
            dateEdit = System.currentTimeMillis()
        }

    var dateEdit: Long = dateEdit
        private set

    override fun toString(): String {
        return "$title: $content"
    }
}

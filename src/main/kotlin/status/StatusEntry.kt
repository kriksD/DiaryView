package status

import HasId
import kotlinx.serialization.Serializable

@Serializable(StatusEntrySerializer::class)
class StatusEntry(
    override val id: Int,
    content: String = "",
    val dateCreate: Long = System.currentTimeMillis(),
    dateEdit: Long = System.currentTimeMillis(),
) : HasId {
    var content: String = content
        set(value) {
            field = value
            dateEdit = System.currentTimeMillis()
        }

    var dateEdit: Long = dateEdit
        private set

    override fun toString(): String = content
}
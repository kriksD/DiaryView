package diary

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

class DiaryEntrySerializer : KSerializer<DiaryEntry> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("diary.DiaryEntry") {
        element<Int>("id")
        element<String>("title")
        element<String>("content")
        element<Long>("dateCreate")
        element<Long>("dateEdit")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): DiaryEntry = decoder.decodeStructure(descriptor) {
        var id: Int? = null
        var title: String? = null
        var content: String? = null
        var dateCreate: Long? = null
        var dateEdit: Long? = null

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                -1 -> break
                0 -> id = decodeIntElement(descriptor, index = 0)
                1 -> title = decodeStringElement(descriptor, index = 1)
                2 -> content = decodeStringElement(descriptor, index = 2)
                3 -> dateCreate = decodeLongElement(descriptor, index = 3)
                4 -> dateEdit = decodeLongElement(descriptor, index = 4)
                else -> throw SerializationException("Unexpected index $index")
            }
        }

        if (id == null || title == null)
            throw MissingFieldException("Id or title", descriptor.serialName)

        return@decodeStructure DiaryEntry(
            id = id,
            title = title,
            content = content ?: "",
            dateCreate = dateCreate ?: System.currentTimeMillis(),
            dateEdit = dateEdit ?: System.currentTimeMillis(),
        )
    }

    override fun serialize(encoder: Encoder, value: DiaryEntry) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.id)
        encodeStringElement(descriptor, 1, value.title)
        encodeStringElement(descriptor, 2, value.content)
        encodeLongElement(descriptor, 3, value.dateCreate)
        encodeLongElement(descriptor, 4, value.dateEdit)
    }
}
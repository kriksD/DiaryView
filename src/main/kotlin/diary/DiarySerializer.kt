package diary

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

class DiarySerializer : KSerializer<Diary> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("diary.Diary") {
        element<Int>("id")
        element<String>("title")
        element<String>("description")
        element<List<DiaryEntry>>("entries")
    }
    private val listSerializer = ListSerializer(DiaryEntrySerializer())

    override fun deserialize(decoder: Decoder): Diary = decoder.decodeStructure(descriptor) {
        var diary: Diary? = null

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                -1 -> break
                0 -> {
                    val id = decodeIntElement(descriptor, 0)
                    diary = Diary(id , "", "")
                }
                1 -> diary?.title = decodeStringElement(descriptor, 1)
                2 -> diary?.description = decodeStringElement(descriptor, 2)
                3 -> decodeSerializableElement(descriptor, 3, listSerializer).forEach { diary?.addEntry(it) }
                else -> throw SerializationException("Unexpected index $index")
            }
        }

        return@decodeStructure diary ?: throw SerializationException("Missing field")
    }

    override fun serialize(encoder: Encoder, value: Diary) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.id)
        encodeStringElement(descriptor, 1, value.title)
        encodeStringElement(descriptor, 2, value.description)
        encodeSerializableElement(descriptor, 3, listSerializer, value.entries)
    }
}
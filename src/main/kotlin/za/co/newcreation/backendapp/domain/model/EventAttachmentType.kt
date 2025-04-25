package za.co.newcreation.backendapp.domain.model

@JvmInline
value class EventAttachmentType(val name: String) {
    override fun toString(): String = name
}

val COVER_IMAGE = EventAttachmentType("COVER_IMAGE")
val DOCUMENT = EventAttachmentType("DOCUMENT")
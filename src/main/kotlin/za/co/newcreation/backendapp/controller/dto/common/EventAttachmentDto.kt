package za.co.newcreation.backendapp.controller.dto.common

import kotlinx.serialization.Serializable
import za.co.newcreation.backendapp.domain.model.EventAttachment
import za.co.newcreation.backendapp.domain.model.EventAttachmentType
import java.time.OffsetDateTime

@Serializable
data class EventAttachmentDto(
    var attachmentUrl: String,
    var attachmentFilename: String,
    var attachmentPublicId: String,
    var attachmentType: String,
    var eventDate: String
) {
    fun toDomainModel(): EventAttachment =
        EventAttachment(
            attachmentUrl = attachmentUrl,
            attachmentFilename = attachmentFilename,
            attachmentPublicId = attachmentPublicId,
            attachmentType = EventAttachmentType(attachmentType),
            eventDate = OffsetDateTime.parse(eventDate)
        )
}

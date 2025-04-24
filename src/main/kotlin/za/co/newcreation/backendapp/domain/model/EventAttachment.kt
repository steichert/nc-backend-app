package za.co.newcreation.backendapp.domain.model

import za.co.newcreation.backendapp.controller.dto.common.EventAttachmentDto
import za.co.newcreation.backendapp.repository.dao.EventAttachmentDao
import java.time.OffsetDateTime

data class EventAttachment (
    val id: Long? = null,
    var attachmentUrl: String,
    var attachmentFilename: String,
    var attachmentPublicId: String,
    var attachmentType: EventAttachmentType,
    var eventDate: OffsetDateTime
) {
    lateinit var eventId: String

    fun toDao(): EventAttachmentDao =
        EventAttachmentDao(
            id = id,
            attachmentUrl = attachmentUrl,
            attachmentFilename = attachmentFilename,
            attachmentPublicId = attachmentPublicId,
            attachmentType = attachmentType.name,
            eventDate = eventDate,
            eventId = eventId.toLong()
        )

    fun toDto(): EventAttachmentDto =
        EventAttachmentDto(
            attachmentUrl = attachmentUrl,
            attachmentFilename = attachmentFilename,
            attachmentPublicId = attachmentPublicId,
            attachmentType = attachmentType.name,
            eventDate = eventDate.toString()
        )
}
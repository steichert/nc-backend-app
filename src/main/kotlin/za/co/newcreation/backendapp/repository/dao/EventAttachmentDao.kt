package za.co.newcreation.backendapp.repository.dao

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import za.co.newcreation.backendapp.domain.model.EventAttachment
import za.co.newcreation.backendapp.domain.model.EventAttachmentType
import java.time.OffsetDateTime

@Table(name = "EventAttachments")
class EventAttachmentDao(
    @Id
    @Column(value = "Id")
    var id: Long? = null,

    @Column(value = "AttachmentUrl")
    var attachmentUrl: String,

    @Column(value = "AttachmentFilename")
    var attachmentFilename: String,

    @Column(value = "AttachmentPublicId")
    var attachmentPublicId: String,

    @Column(value = "AttachmentType")
    var attachmentType: String,

    @Column(value = "EventDate")
    var eventDate: OffsetDateTime,

    @Column(value = "EventId")
    val eventId: Long
) {
    fun toDomainModel(): EventAttachment {
        val eventAttachment = EventAttachment(
            id = id,
            attachmentUrl = attachmentUrl,
            attachmentFilename = attachmentFilename,
            attachmentPublicId = attachmentPublicId,
            attachmentType = EventAttachmentType(attachmentType),
            eventDate = eventDate
        )
        eventAttachment.eventId = eventId.toString()
        return eventAttachment
    }
}
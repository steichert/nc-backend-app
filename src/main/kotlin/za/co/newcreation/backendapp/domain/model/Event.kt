package za.co.newcreation.backendapp.domain.model

import za.co.newcreation.backendapp.controller.dto.EventResponseDto
import za.co.newcreation.backendapp.repository.dao.EventDao
import java.time.OffsetDateTime

data class Event (
    val id: Long? = null,
    var title: String,
    var description: String? = null,
    var state: EventState,
    var eventDate: OffsetDateTime,
    var actionLabel: String? = null,
    var actionUrl: String? = null,
    val creationDate: OffsetDateTime,
    val publicId: Long,
    var modifiedDate: OffsetDateTime? = null,
    var eventAttachments: List<EventAttachment>
) {
    fun toDao() =
        EventDao(
            id = id,
            title = title,
            description = description,
            state = state.name,
            eventDate = eventDate,
            actionLabel = actionLabel,
            actionUrl = actionUrl,
            publicId = publicId,
            creationDate = creationDate.toLocalDateTime(),
            modifiedDate = modifiedDate?.toLocalDateTime()
        )

    fun toResponseDto() =
        EventResponseDto(
            title = title,
            description = description,
            state = state.name,
            eventDate = eventDate.toString(),
            eventAttachments = eventAttachments.map { it.toDto() },
            publicId = publicId
        )
}
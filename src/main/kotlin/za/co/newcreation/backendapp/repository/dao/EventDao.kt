package za.co.newcreation.backendapp.repository.dao

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import za.co.newcreation.backendapp.domain.model.Event
import za.co.newcreation.backendapp.domain.model.EventState
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

@Table(name = "Events")
class EventDao(
    @Id
    @Column(value = "Id")
    var id: Long? = null,

    @Column(value = "Title")
    var title: String,

    @Column(value = "Description")
    var description: String? = null,

    @Column(value = "State")
    var state: String,

    @Column(value = "EventDate")
    var eventDate: OffsetDateTime,

    @Column(value = "ActionLabel")
    var actionLabel: String? = null,

    @Column(value = "ActionUrl")
    var actionUrl: String? = null,

    @Column(value = "PublicId")
    val publicId: Long,

    @CreatedDate
    @Column(value = "CreationDate")
    val creationDate: LocalDateTime,

    @LastModifiedDate
    @Column(value = "ModifiedDate")
    var modifiedDate: LocalDateTime? = null
) {
    fun toDomainModel() =
        Event(
            id = id,
            title = title,
            description = description,
            state = EventState(state),
            eventDate = eventDate,
            actionLabel = actionLabel,
            actionUrl = actionUrl,
            publicId = publicId,
            creationDate = creationDate.atZone(ZoneId.systemDefault()).toOffsetDateTime(),
            modifiedDate = modifiedDate?.atZone(ZoneId.systemDefault())?.toOffsetDateTime(),
            eventAttachments = emptyList()
        )
}
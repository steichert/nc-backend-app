package za.co.newcreation.backendapp.controller.dto

import kotlinx.serialization.Serializable
import za.co.newcreation.backendapp.controller.dto.common.EventAttachmentDto

@Serializable
data class CreateEventRequestDto(
    val title: String,
    val description: String? = null,
    val state: String,
    val eventDate: String,
    val actionLabel: String? = null,
    val actionUrl: String? = null,
    val eventAttachments: List<EventAttachmentDto>
)
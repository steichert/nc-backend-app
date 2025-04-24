package za.co.newcreation.backendapp.controller.integration.step

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import za.co.newcreation.backendapp.controller.dto.CreateEventRequestDto
import za.co.newcreation.backendapp.controller.dto.EventResponseDto
import za.co.newcreation.backendapp.controller.integration.IntegrationTest
import za.co.newcreation.backendapp.controller.integration.common.ScenarioState
import za.co.newcreation.backendapp.domain.model.EventAttachment
import za.co.newcreation.backendapp.domain.model.EventState
import za.co.newcreation.backendapp.repository.EventAttachmentRepository
import za.co.newcreation.backendapp.repository.EventRepository
import za.co.newcreation.backendapp.repository.dao.EventAttachmentDao
import za.co.newcreation.backendapp.repository.dao.EventDao
import java.time.OffsetDateTime

@Component
class EventStep(
    @Autowired private val scenarioState: ScenarioState,
    @Autowired private val eventRepository: EventRepository,
    @Autowired private val eventAttachmentRepository: EventAttachmentRepository,
    @Autowired private val mockMvc: MockMvc
): IntegrationTest(mockMvc) {

    suspend fun givenExistingEvent(
        title: String,
        description: String,
        state: EventState,
        eventDate: OffsetDateTime,
        actionLabel: String? = null,
        actionUrl: String? = null,
        eventAttachments: List<EventAttachment>? = emptyList(),
        publicId: Long
    ) {
        if (eventRepository.findByPublicId(publicId) == null) {
            val event = eventRepository.save(EventDao(
                title = title,
                description = description,
                state = state.name,
                eventDate = eventDate,
                actionLabel = actionLabel,
                actionUrl = actionUrl,
                publicId = publicId,
                creationDate = OffsetDateTime.now().toLocalDateTime()
            ))

            eventAttachments?.forEach { eventAttachment ->
                eventAttachmentRepository.save(
                    EventAttachmentDao(
                        attachmentUrl = eventAttachment.attachmentUrl,
                        attachmentFilename = eventAttachment.attachmentFilename,
                        attachmentPublicId = eventAttachment.attachmentPublicId,
                        attachmentType = eventAttachment.attachmentType.name,
                        eventDate = eventDate,
                        eventId = event.id!!
                    )
                )
            }
        }
    }

    fun whenCreateEventIsRequested(
        requestId: String,
        title: String,
        description: String,
        state: EventState,
        eventDate: String,
        actionLabel: String? = null,
        actionUrl: String? = null,
        eventAttachments: List<EventAttachment>? = emptyList(),
    ) {
        scenarioState.requestId = requestId

        scenarioState.resultMap[requestId] = postRequest(
            path = EVENT_BASE_URL,
            body = Json.encodeToString(CreateEventRequestDto(
                title = title,
                description = description,
                state = state.name,
                eventDate = eventDate,
                actionLabel = actionLabel,
                actionUrl = actionUrl,
                eventAttachments = eventAttachments?.map { it.toDto() }.orEmpty()
            ))
        )
    }

    fun thenGetEventByPublicIdIsRequested(requestId: String, publicId: Long) {
        scenarioState.requestId = requestId
        scenarioState.resultMap[requestId] = getRequest("$EVENT_BASE_URL/$publicId")
    }

    fun getEventResponseDto(requestId: String): EventResponseDto {
        return Json.decodeFromString<EventResponseDto>(scenarioState.resultMap[requestId]?.response?.contentAsString!!)
    }
}
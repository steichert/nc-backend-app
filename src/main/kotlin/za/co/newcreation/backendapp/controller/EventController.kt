package za.co.newcreation.backendapp.controller

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import za.co.newcreation.backendapp.controller.dto.CreateEventRequestDto
import za.co.newcreation.backendapp.controller.dto.EventResponseDto
import za.co.newcreation.backendapp.domain.model.Event
import za.co.newcreation.backendapp.domain.model.EventState
import za.co.newcreation.backendapp.service.EventService
import za.co.newcreation.backendapp.util.PublicIdUtil
import java.time.OffsetDateTime

@Component
@RestController
class EventController(
    private val eventService: EventService
) {

    @GetMapping("/event", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    suspend fun getAllEvents(
        @RequestParam(value = "includeAttachments") includeAttachments: Boolean? = true
    ): ResponseEntity<List<EventResponseDto>> = coroutineScope {
        val events = async { eventService.findAllEvents(includeAttachments = includeAttachments) }

        ResponseEntity.ok(
            events.await().map { it.toResponseDto() }
        )
    }

    @GetMapping("/event/{publicId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    suspend fun getEventByPublicId(
        @PathVariable(value = "publicId") pubicId: String
    ): ResponseEntity<Any> = coroutineScope {
        val event = async { eventService.findByPublicId(pubicId) }.await()

        if (event == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build()
        } else {
            ResponseEntity.ok(event.toResponseDto())
        }
    }

    @PostMapping("/event", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    suspend fun createEvent(
        @RequestBody requestDto: CreateEventRequestDto
    ): ResponseEntity<EventResponseDto> = coroutineScope {
        val event = async {
            eventService.save(
                Event(
                    title = requestDto.title,
                    description = requestDto.description,
                    state = EventState(requestDto.state),
                    eventDate = OffsetDateTime.parse(requestDto.eventDate),
                    eventAttachments = requestDto.eventAttachments.map { it.toDomainModel() },
                    creationDate = OffsetDateTime.now(),
                    publicId = PublicIdUtil.generatePublicId()
                )
            )
        }

        ResponseEntity.ok(
            event.await().toResponseDto()
        )
    }
}
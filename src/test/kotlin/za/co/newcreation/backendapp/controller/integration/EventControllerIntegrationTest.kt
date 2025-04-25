package za.co.newcreation.backendapp.controller.integration

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import za.co.newcreation.backendapp.Application
import za.co.newcreation.backendapp.controller.integration.config.IntegrationTestConfiguration
import za.co.newcreation.backendapp.controller.integration.step.EventStep
import za.co.newcreation.backendapp.controller.integration.step.ResultStep
import za.co.newcreation.backendapp.domain.model.ACTIVE
import za.co.newcreation.backendapp.domain.model.COVER_IMAGE
import za.co.newcreation.backendapp.domain.model.DOCUMENT
import za.co.newcreation.backendapp.domain.model.EventAttachment
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.random.Random

@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class, IntegrationTestConfiguration::class]
)
class EventControllerIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val eventStep: EventStep,
    @Autowired private val resultStep: ResultStep
): IntegrationTest(mockMvc) {

    @Test
    fun `should create a new event without attachments`(): Unit = runBlocking {
        // Given
        val requestId = UUID.randomUUID().toString()
        val title = "Event without attachments"
        val eventDate = OffsetDateTime.now().plusWeeks(1).toString()

        // When
        eventStep.whenCreateEventIsRequested(
            requestId = requestId,
            title = title,
            description = "This is the description for an upcoming event.",
            eventDate = eventDate,
            state = ACTIVE,
            actionLabel = "Register",
            actionUrl = "www.example.com",
            eventAttachments = emptyList()
        )

        // Then
        resultStep.thenResponseStatusIs(HttpStatus.OK.value())
        resultStep.thenResponseContentIsNotNull()

        val responseDto = eventStep.getEventResponseDto(requestId)
        assertThat(responseDto).isNotNull
        assertThat(responseDto.title).isEqualTo(title)
        assertThat(responseDto.eventDate).isEqualTo(eventDate)
        assertThat(responseDto.eventAttachments).isEmpty()
    }

    @Test
    fun `should create a new event with attachments`(): Unit = runBlocking {
        // Given
        val requestId = UUID.randomUUID().toString()
        val title = "Event with attachments"
        val eventDate = OffsetDateTime.now().plusWeeks(1)
        val eventAttachments = listOf(
            EventAttachment(
                attachmentUrl = "www.example.com/image",
                attachmentFilename = "image.png",
                attachmentPublicId = "/image",
                attachmentType = COVER_IMAGE,
                eventDate = eventDate
            ),
            EventAttachment(
                attachmentUrl = "www.example.com/calendar",
                attachmentFilename = "calendar.doc",
                attachmentPublicId = "/calendar",
                attachmentType = DOCUMENT,
                eventDate = eventDate
            )
        )

        // When
        eventStep.whenCreateEventIsRequested(
            requestId = requestId,
            title = title,
            description = "This is the description for an upcoming event with attachments.",
            eventDate = eventDate.toString(),
            state = ACTIVE,
            actionLabel = "Sign Up",
            actionUrl = "www.example.com/sign-up",
            eventAttachments = eventAttachments
        )

        // Then
        resultStep.thenResponseStatusIs(HttpStatus.OK.value())
        resultStep.thenResponseContentIsNotNull()

        val responseDto = eventStep.getEventResponseDto(requestId)
        assertThat(responseDto).isNotNull
        assertThat(responseDto.title).isEqualTo(title)
        assertThat(responseDto.eventDate).isEqualTo(eventDate.toString())
        assertThat(responseDto.eventAttachments).isNotEmpty
        assertThat(responseDto.eventAttachments.find {
            eventAttachmentDto -> eventAttachmentDto.attachmentType == COVER_IMAGE.name
        }).isNotNull
        assertThat(responseDto.eventAttachments.find {
                eventAttachmentDto -> eventAttachmentDto.attachmentType == DOCUMENT.name
        }).isNotNull
    }

    @Test
    fun `should fetch event by publicId`(): Unit = runBlocking {
        // Given
        val requestId = UUID.randomUUID().toString()
        val publicId = Random.nextLong(1000000000, 9999999999)
        val title = "Event with attachments"
        val eventDate = OffsetDateTime.now().plusWeeks(1)
        val eventAttachments = listOf(
            EventAttachment(
                attachmentUrl = "www.example.com/image",
                attachmentFilename = "image.png",
                attachmentPublicId = "/image",
                attachmentType = COVER_IMAGE,
                eventDate = eventDate
            )
        )

        eventStep.givenExistingEvent(
            title = title,
            description = "Event description",
            eventDate = eventDate,
            state = ACTIVE,
            publicId = publicId,
            eventAttachments = eventAttachments,
            actionLabel = "Register",
            actionUrl = "www.example.com/register"
        )

        // When
        eventStep.thenGetEventByPublicIdIsRequested(requestId, publicId)

        // Then
        val responseDto = eventStep.getEventResponseDto(requestId)
        assertThat(responseDto).isNotNull
        assertThat(responseDto.title).isEqualTo(title)
        assertThat(responseDto.eventDate).isEqualTo(eventDate.toString())
        assertThat(responseDto.eventAttachments).isNotEmpty
        assertThat(responseDto.eventAttachments.find {
                eventAttachmentDto -> eventAttachmentDto.attachmentType == COVER_IMAGE.name
        }).isNotNull
    }
}
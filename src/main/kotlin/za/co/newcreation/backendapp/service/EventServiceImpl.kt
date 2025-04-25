package za.co.newcreation.backendapp.service

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import za.co.newcreation.backendapp.domain.model.Event
import za.co.newcreation.backendapp.repository.EventAttachmentRepository
import za.co.newcreation.backendapp.repository.EventRepository

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository,
    private val eventAttachmentRepository: EventAttachmentRepository
) : EventService {

    override suspend fun save(event: Event): Event = coroutineScope {
        val savedEvent = eventRepository.save(event.toDao()).toDomainModel()

        val savedEventAttachments = async {
            eventAttachmentRepository.saveAll(event.eventAttachments.map {
                it.eventId = savedEvent.id!!.toString()
                it.toDao()
            })
        }

        savedEvent.eventAttachments = savedEventAttachments.await().map { it.toDomainModel() }.toList()
        savedEvent
    }

    override suspend fun findAllEvents(includeAttachments: Boolean?): List<Event> = coroutineScope {
        val events = eventRepository.findAll().map { it.toDomainModel() }.toList()

        if (includeAttachments == true) {
            events.map { event ->
                val eventAttachments = async { eventAttachmentRepository.findAllByEventId(event.id!!) }
                event.eventAttachments = eventAttachments.await().map { it.toDomainModel() }
            }
        }

        events
    }

    override suspend fun findByPublicId(publicId: String): Event? = coroutineScope {
        val event = eventRepository.findByPublicId(publicId.toLong())?.toDomainModel()

        event?.let {
            val eventAttachments = async { eventAttachmentRepository.findAllByEventId(it.id!!) }
            event.eventAttachments = eventAttachments.await().map { it.toDomainModel() }
        }

        event
    }
}
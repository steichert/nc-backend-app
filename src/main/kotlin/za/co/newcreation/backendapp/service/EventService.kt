package za.co.newcreation.backendapp.service

import za.co.newcreation.backendapp.domain.model.Event

interface EventService {

    suspend fun save(event: Event): Event

    suspend fun findAllEvents(includeAttachments: Boolean? = false): List<Event>

    suspend fun findByPublicId(publicId: String): Event?
}
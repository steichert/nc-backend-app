package za.co.newcreation.backendapp.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import za.co.newcreation.backendapp.repository.dao.EventAttachmentDao

@Repository
interface EventAttachmentRepository: CoroutineCrudRepository<EventAttachmentDao, Long> {

    suspend fun findAllByEventId(eventId: Long): List<EventAttachmentDao>
}
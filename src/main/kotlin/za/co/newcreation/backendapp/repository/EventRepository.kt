package za.co.newcreation.backendapp.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import za.co.newcreation.backendapp.repository.dao.EventDao

@Repository
interface EventRepository: CoroutineCrudRepository<EventDao, Long> {

    suspend fun findByPublicId(publicId: Long): EventDao?
}
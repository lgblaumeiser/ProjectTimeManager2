package de.lgblaumeiser.ptm.persistence

import de.lgblaumeiser.ptm.service.model.Booking
import de.lgblaumeiser.ptm.service.store.BookingStore
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ApplicationScoped
class BookingDataStore : BookingStore {

    @Inject
    val manager: EntityManager

    override fun retrieveByDate(startdate: LocalDate, enddate: LocalDate?): List<Booking> {
        TODO("Not yet implemented")
    }

    override fun retrieveAll(): List<Booking> {
        TODO("Not yet implemented")
    }

    override fun retrieveById(id: Long): Booking {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun create(data: Booking): Long {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun update(data: Booking) {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}
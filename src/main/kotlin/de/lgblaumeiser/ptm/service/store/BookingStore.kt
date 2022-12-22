package de.lgblaumeiser.ptm.service.store

import de.lgblaumeiser.ptm.service.model.Booking
import java.time.LocalDate

interface BookingStore : Store<Booking> {
    fun retrieveByDate(startdate: LocalDate, enddate: LocalDate?) : List<Booking>
}
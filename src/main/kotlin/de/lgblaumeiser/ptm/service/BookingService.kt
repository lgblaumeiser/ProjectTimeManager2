// SPDX-FileCopyrightText: 2020 Lars Geyer-Blaumeiser <lars@lgblaumeiser.de>
// SPDX-License-Identifier: MIT
package de.lgblaumeiser.ptm.service

import de.lgblaumeiser.ptm.service.model.Booking
import de.lgblaumeiser.ptm.service.store.BookingStore
import java.time.LocalDate
import java.time.LocalTime

class BookingService(val store: BookingStore) {
    fun getBookings(user: String) = store
        .retrieveAll()
        .filter { sameUser(it.user, user) }
        .sortedWith(compareBy<Booking> { it.starttime }.thenBy { it.bookingday })

    fun getBookings(user: String, startday: LocalDate, endday: LocalDate? = null): List<Booking> = store
        .retrieveByDate(startday, endday)
        .filter { sameUser(it.user, user) }
        .sortedWith(compareBy<Booking> { it.starttime }.thenBy { it.bookingday })

    fun getBookingById(user: String, id: Long) = store
        .retrieveById(id)
        .ownedByUserOrException(user)

    fun addBooking(
        user: String,
        bookingday: String,
        starttime: String,
        endtime: String? = null,
        activity: Long,
        comment: String = ""
    ): Long {
        val day = LocalDate.parse(bookingday)
        retrieveOpenBooking(user, day)?.let { changeBooking(id = it.id, user = it.user, endtime = starttime) }
        return store.create(
            Booking(
                user = user,
                bookingday = day,
                starttime = LocalTime.parse(starttime),
                endtime = endtime?.let { LocalTime.parse(endtime) },
                activity = activity,
                comment = comment
            )
        )
    }

    private fun retrieveOpenBooking(user: String, bookingday: LocalDate) =
        getBookings(user = user, startday = bookingday).find { it.endtime == null }


    fun changeBooking(
        user: String,
        bookingday: String? = null,
        starttime: String? = null,
        endtime: String? = null,
        activity: Long? = null,
        comment: String? = null,
        id: Long
    ) = getBookingById(user, id).let {
        store.update(
            Booking(
                id = it.id,
                user = it.user,
                bookingday = LocalDate.parse(bookingday) ?: it.bookingday,
                starttime = LocalTime.parse(starttime) ?: it.starttime,
                endtime = LocalTime.parse(endtime) ?: it.endtime,
                activity = activity ?: it.activity,
                comment = comment ?: it.comment
            )
        )
    }

    fun splitBooking(
        user: String,
        starttime: String,
        duration: Long = 30L,
        id: Long
    ): Long {
        val booking = getBookingById(user, id)
        val parsedstarttime = LocalTime.parse(starttime)
        val firstBooking = booking.copy(endtime = parsedstarttime)
        val secondBooking = booking.copy(starttime = parsedstarttime.plusMinutes(duration))
        store.update(firstBooking)
        return store.create(secondBooking)
    }

    fun deleteBooking(user: String, id: Long) =
        getBookingById(user, id).let { store.delete(id) }
}

fun Booking.ownedByUserOrException(requester: String): Booking {
    if (differentUser(this.user, requester)) {
        throw UserAccessException("User mismatch, resource does not belong to $requester")
    }
    return this
}

package org.eventmanagement.repository;

import java.awt.print.Book;
import java.util.List;
import java.util.UUID;

import org.eventmanagement.enums.BookingStatus;
import org.eventmanagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface BookingRepository extends CrudRepository<Booking, UUID>, JpaRepository<Booking, UUID> {
    List<Booking> findAllByBookedBy(String whoAmI);

    List<Booking> findAllByEventIdAndBookingStatus(long eventId, BookingStatus bookingStatus);
}

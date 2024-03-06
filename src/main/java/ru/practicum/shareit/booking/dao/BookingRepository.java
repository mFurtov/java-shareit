package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.modul.Booking;

public interface BookingRepository  extends JpaRepository<Booking, Integer> {
}

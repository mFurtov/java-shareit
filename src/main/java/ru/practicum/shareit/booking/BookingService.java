package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

public interface BookingService {
    public BookingDto postBooking(int id, BookingDtoRequest bookingDtoRequest);
}

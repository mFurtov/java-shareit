package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

public interface BookingService {
    BookingDto postBooking(int id, BookingDtoRequest bookingDtoRequest);

    BookingDto patchBookingApproved(int userId, int bookingId, boolean approved);

    BookingDto getBooking(int bookingId);
}

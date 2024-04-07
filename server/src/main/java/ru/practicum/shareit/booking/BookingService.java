package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {
    BookingDto postBooking(int id, BookingDtoRequest bookingDtoRequest);

    BookingDto patchBookingApproved(int userId, int bookingId, boolean approved);

    BookingDto getBooking(int id, int bookingId);

    List<BookingDto> getBookingWithStatus(int requestId, String state, Pageable pageable);

    List<BookingDto> getBookingWithStatusOwner(int requestId, String state, Pageable pageable);

}

package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {
    BookingDto postBooking(int id, BookingDtoRequest bookingDtoRequest);

    BookingDto patchBookingApproved(int userId, int bookingId, boolean approved);

    BookingDto getBooking(int id, int bookingId);

    List<BookingDto> getBookingWithStatus(int requestId, String state);

    List<BookingDto> getBookingWithStatusOwner(int requestId, String state);

}

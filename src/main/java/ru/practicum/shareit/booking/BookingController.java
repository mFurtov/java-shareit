package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.user.model.User;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;
    @PostMapping
    public BookingDto postBooking(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int id, @RequestBody BookingDtoRequest bookingDtoRequest) {
        service.postBooking(id,bookingDtoRequest);
        return null;
    }
}

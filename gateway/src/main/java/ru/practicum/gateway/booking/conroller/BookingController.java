package ru.practicum.gateway.booking.conroller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.HeaderConstants;
import ru.practicum.gateway.booking.client.BookingClient;
import ru.practicum.gateway.booking.dto.BookingDtoRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    @Autowired
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long id, @Validated(Create.class) @RequestBody BookingDtoRequest bookingDtoRequest) {
        return bookingClient.bookItem(id, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchBookingApproved(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @PathVariable Long bookingId, @RequestParam boolean approved) {
        return bookingClient.patchBooking(userId, bookingId, approved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBooking(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @PathVariable Long id) {
        return bookingClient.getBooking(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingWithStatus(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long requestId, @RequestParam(defaultValue = "ALL") String state, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "100", required = false) @Min(1) Integer size) {
        return bookingClient.getBookings(requestId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingWithStatusOwner(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long requestId, @RequestParam(defaultValue = "ALL") String state, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "100", required = false) @Min(1) Integer size) {
        return bookingClient.getBookingsOwner(requestId, state, from, size);
    }
}

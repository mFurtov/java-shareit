package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;
    @PostMapping
    public BookingDto postBooking(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int id,@Validated(Create.class) @RequestBody BookingDtoRequest bookingDtoRequest) {
        return service.postBooking(id,bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchBookingApproved(@RequestHeader(HeaderConstants.X_SHARER_USER_ID)int userId, @PathVariable int bookingId, @RequestParam boolean approved){
       return service.patchBookingApproved(userId,bookingId,approved);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable int bookingId){

    }

}

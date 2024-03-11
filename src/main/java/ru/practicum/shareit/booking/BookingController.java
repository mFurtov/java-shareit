package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingDto postBooking(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int id, @Validated(Create.class) @RequestBody BookingDtoRequest bookingDtoRequest) {
        BookingDto bookingDto = service.postBooking(id, bookingDtoRequest);
        log.info("Заказ c id \"{}\" добвлен", bookingDto.getId());
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchBookingApproved(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @PathVariable int bookingId, @RequestParam boolean approved) {
        BookingDto bookingDto = service.patchBookingApproved(userId, bookingId, approved);
        log.info("Заказ c id \"{}\" обновлен", bookingDto.getId());
        return bookingDto;
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int requestId, @PathVariable int id) {
        BookingDto bookingDto = service.getBooking(requestId, id);
        log.info("Заказ c id \"{}\" выведен", bookingDto.getId());
        return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getBookingWithStatus(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int requestId, @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookingDto = service.getBookingWithStatus(requestId, state);
        log.info("Список заазов выведен \"{}\" ", bookingDto.size());
        return bookingDto;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingWithStatusOwner(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int requestId, @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookingDto = service.getBookingWithStatusOwner(requestId, state);
        log.info("Список заазов владельца выведен \"{}\" ", bookingDto.size());
        return bookingDto;
    }
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.pageable.PageableCreate;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
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
    public BookingDto getBooking(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @PathVariable int id) {
        BookingDto bookingDto = service.getBooking(userId, id);
        log.info("Заказ c id \"{}\" выведен", bookingDto.getId());
        return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getBookingWithStatus(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @RequestParam(defaultValue = "ALL") String state, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {
        List<BookingDto> bookingDto = service.getBookingWithStatus(userId, state, PageableCreate.getPageable(from, size, Sort.by(Sort.Direction.DESC, "start")));
        log.info("Список заазов выведен \"{}\" ", bookingDto.size());
        return bookingDto;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingWithStatusOwner(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @RequestParam(defaultValue = "ALL") String state, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {
        Sort.by("id").ascending();
        List<BookingDto> bookingDto = service.getBookingWithStatusOwner(userId, state, PageableCreate.getPageable(from, size, Sort.by(Sort.Direction.DESC, "start")));
        log.info("Список заазов владельца выведен \"{}\" ", bookingDto.size());
        return bookingDto;
    }
}

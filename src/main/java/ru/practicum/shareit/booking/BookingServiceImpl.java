package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.ItemService;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemService itemService;
    private final UserService userService;

    public BookingDto postBooking(int id, BookingDtoRequest bookingDtoRequest) {
        checkDate(bookingDtoRequest.getStart(), bookingDtoRequest.getEnd());
        User user = UserMapper.fromUserDto(userService.getUserById(id));
        Item item = itemService.getItemNDto(bookingDtoRequest.getItemId());
        checkAvailable(item, id);
        Booking booking = new Booking(bookingDtoRequest.getStart(), bookingDtoRequest.getEnd(), item, user, BookingStatus.WAITING);
        return BookingMapper.mapToBookingDto(repository.save(booking));
    }

    public BookingDto patchBookingApproved(int userId, int bookingId, boolean approved) {
        Booking booking = repository.getById(bookingId);
        if (approved && booking.getStatus().toString().equals("APPROVED")) {
            log.error("Статус вещи уже соответствует запрашиваемому");
            throw new ValidException("Статус вещи уже соответствует запрашиваемому");
        }
        if (booking.getItem().getOwner().getId() == userId) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }

            return BookingMapper.mapToBookingDto(repository.save(booking));
        } else {
            log.error("Пользователь не владелец вещи");
            throw new OwnerException("Пользователь не владелец вещи");
        }

    }

    public BookingDto getBooking(int requestId, int bookingId) {
        return BookingMapper.mapToBookingDto(repository.searchBooking(requestId, bookingId));
    }

    @Override
    public List<BookingDto> getBookingWithStatus(int requestId, String state) {
        UserDto user = userService.getUserById(requestId);
        final LocalDateTime dataNow = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return BookingMapper.mapToListUserDto(repository.findByBookerIdOrderByStartDesc(requestId));
            case "CURRENT":
                return BookingMapper.mapToListUserDto(repository.findCurrentBookings(dataNow, requestId));
            case "PAST":
                return BookingMapper.mapToListUserDto(repository.findPastBookings(dataNow, requestId));
            case "FUTURE":
                return BookingMapper.mapToListUserDto(repository.findFutureBookings(dataNow, requestId));
            case "WAITING":
                return BookingMapper.mapToListUserDto(repository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.WAITING, requestId));
            case "REJECTED":
                return BookingMapper.mapToListUserDto(repository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.REJECTED, requestId));
            default:
                log.error("Unknown state: UNSUPPORTED_STATUS");
                throw new ValidException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<BookingDto> getBookingWithStatusOwner(int requestId, String state) {
        UserDto user = userService.getUserById(requestId);
        final LocalDateTime dataNow = LocalDateTime.now();

        switch (state) {
            case "ALL":
                return BookingMapper.mapToListUserDto(repository.findByItemOwnerIdOrderByStartDesc(requestId));
            case "CURRENT":
                return BookingMapper.mapToListUserDto(repository.findCurrentBookingsOwner(dataNow, requestId));
            case "PAST":
                return BookingMapper.mapToListUserDto(repository.findPastBookingsOwner(dataNow, requestId));
            case "FUTURE":
                return BookingMapper.mapToListUserDto(repository.findFutureBookingsOwner(dataNow, requestId));
            case "WAITING":
                return BookingMapper.mapToListUserDto(repository.findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus.WAITING, requestId));
            case "REJECTED":
                return BookingMapper.mapToListUserDto(repository.findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus.REJECTED, requestId));
            default:
                log.error("Unknown state: UNSUPPORTED_STATUS");
                throw new ValidException("Unknown state: UNSUPPORTED_STATUS");
        }
    }


    private void checkDate(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            log.error("Ошибка валидации");
            throw new ValidException("Ошибка валидации");
        }
    }

    private void checkAvailable(Item item, int id) {
        if (item.getAvailable().equals(false)) {
            throw new ValidException("Вещь уже забронирована");
        }
        if (item.getOwner().getId() == id) {
            log.error("Владелец вещи не может ее забронировать");
            throw new OwnerException("Владелец вещи не может ее забронировать");
        }
    }

}

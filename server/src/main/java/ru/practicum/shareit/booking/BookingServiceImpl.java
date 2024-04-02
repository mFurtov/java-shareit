package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    public BookingDto postBooking(int id, BookingDtoRequest bookingDtoRequest) {
        User user = UserMapper.fromUserDto(userService.getUserById(id));
        Item item = itemService.getItemNDto(bookingDtoRequest.getItemId());
        checkAvailable(item, id);
        Booking booking = new Booking(bookingDtoRequest.getStart(), bookingDtoRequest.getEnd(), item, user, BookingStatus.WAITING);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    public BookingDto patchBookingApproved(int userId, int bookingId, boolean approved) {
        Booking booking = bookingRepository.getById(bookingId);
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

            return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        } else {
            log.error("Пользователь не владелец вещи");
            throw new OwnerException("Пользователь не владелец вещи");
        }

    }

    public BookingDto getBooking(int userId, int bookingId) {
        bookingRepository.findByIdOrThrow(bookingId);
        return BookingMapper.mapToBookingDto(bookingRepository.searchBookingOrThrow(userId, bookingId));


    }

    @Override
    public List<BookingDto> getBookingWithStatus(int userId, String state, Pageable pageable) {
        UserDto user = userService.getUserById(userId);
        final LocalDateTime dataNow = LocalDateTime.now();

        switch (BookingEnum.getEnum(state)) {
            case ALL:
                return BookingMapper.mapToListUserDto(bookingRepository.findByBookerIdOrderByStartDesc(userId, pageable));
            case CURRENT:
                return BookingMapper.mapToListUserDto(bookingRepository.findCurrentBookings(dataNow, userId, pageable));
            case PAST:
                return BookingMapper.mapToListUserDto(bookingRepository.findPastBookings(dataNow, userId, pageable));
            case FUTURE:
                return BookingMapper.mapToListUserDto(bookingRepository.findFutureBookings(dataNow, userId, pageable));
            case WAITING:
                return BookingMapper.mapToListUserDto(bookingRepository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.WAITING, userId, pageable));
            case REJECTED:
                return BookingMapper.mapToListUserDto(bookingRepository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.REJECTED, userId, pageable));
            default:
                log.error("Unknown state: UNSUPPORTED_STATUS");
                throw new ValidException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<BookingDto> getBookingWithStatusOwner(int requestId, String state, Pageable pageable) {
        UserDto user = userService.getUserById(requestId);
        final LocalDateTime dataNow = LocalDateTime.now();

        switch (BookingEnum.getEnum(state)) {
            case ALL:
                return BookingMapper.mapToListUserDto(bookingRepository.findByItemOwnerIdOrderByStartDesc(requestId, pageable));
            case CURRENT:
                return BookingMapper.mapToListUserDto(bookingRepository.findCurrentBookingsOwner(dataNow, requestId, pageable));
            case PAST:
                return BookingMapper.mapToListUserDto(bookingRepository.findPastBookingsOwner(dataNow, requestId, pageable));
            case FUTURE:
                return BookingMapper.mapToListUserDto(bookingRepository.findFutureBookingsOwner(dataNow, requestId, pageable));
            case WAITING:
                return BookingMapper.mapToListUserDto(bookingRepository.findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus.WAITING, requestId, pageable));
            case REJECTED:
                return BookingMapper.mapToListUserDto(bookingRepository.findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus.REJECTED, requestId, pageable));
            default:
                log.error("Unknown state: UNSUPPORTED_STATUS");
                throw new ValidException("Unknown state: UNSUPPORTED_STATUS");
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

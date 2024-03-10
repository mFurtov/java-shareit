package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

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
        checkAvailable(item,id);
//        itemService.setAvailable(item);
        Booking booking = new Booking(bookingDtoRequest.getStart(), bookingDtoRequest.getEnd(), item, user, BookingStatus.WAITING);
        return BookingMapper.mapToBookingDto(repository.save(booking));
    }

    public BookingDto patchBookingApproved(int userId, int bookingId, boolean approved) {
        Booking booking = repository.getById(bookingId);
        if(approved && booking.getStatus().toString().equals("APPROVED")){
            throw new ValidException("Статус вещи уже соответствует запрашиваемому");
        }
        if (booking.getItem().getOwner().getId() == userId) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else if (!(approved)) {
                booking.setStatus(BookingStatus.REJECTED);
            }

            return BookingMapper.mapToBookingDto(repository.save(booking));
        } else {
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
        List<Booking> booking = repository.findByBookerIdOrderByStartDesc(requestId);
        List<Booking> booking2 = repository.findPastBookings(dataNow, requestId);
        List<Booking> booking3 = repository.findFutureBookings(dataNow, requestId);
        List<Booking> booking4 = repository.findCurrentBookings(dataNow, requestId);
        List<Booking> booking5 = repository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.WAITING, requestId);
        List<Booking> booking6 = repository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.REJECTED, requestId);
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
                throw new ValidException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<BookingDto> getBookingWithStatusOwner(int requestId, String state) {

        UserDto user = userService.getUserById(requestId);
        final LocalDateTime dataNow = LocalDateTime.now();
        List<Booking> booking = repository.findByItemOwnerIdOrderByStartDesc(requestId);
        List<Booking> booking2 = repository.findPastBookingsOwner(dataNow, requestId);
        List<Booking> booking3 = repository.findFutureBookingsOwner(dataNow, requestId);
        List<Booking> booking4 = repository.findCurrentBookingsOwner(dataNow, requestId);
        List<Booking> booking5 = repository.findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus.WAITING, requestId);
        List<Booking> booking6 = repository.findByStatusAndItemOwnerIdOrderByStartDesc(BookingStatus.REJECTED, requestId);
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
                throw new ValidException("Unknown state: UNSUPPORTED_STATUS");
        }
    }


    private void checkDate(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new ValidException("Ошибка валидации");
        }
    }

    private void checkAvailable(Item item,int id) {
        if (item.getAvailable().equals(false)) {
            throw new ValidException("Вещь уже забронирована");
        }
        if(item.getOwner().getId()==id){
            throw new OwnerException("Владелец вещи не может ее забронировать");
        }
    }

}

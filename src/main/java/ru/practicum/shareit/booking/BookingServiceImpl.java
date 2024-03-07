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
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

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
        checkAvailable(item);
        Booking booking = new Booking(bookingDtoRequest.getStart(), bookingDtoRequest.getEnd(), item, user, BookingStatus.WAITING);
        return BookingMapper.mapToBookingDto(repository.save(booking));
    }

    public BookingDto patchBookingApproved(int userId, int bookingId, boolean approved){
        Booking booking = repository.getById(bookingId);
        if(booking.getItem().getOwner().getId()==userId){
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else if (!(approved)) {
                booking.setStatus(BookingStatus.REJECTED);
            }
        }
        return BookingMapper.mapToBookingDto(repository.save(booking));
    }

    public BookingDto getBooking(int bookingId){

    }
    private void checkDate(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new ValidException("Ошибка валидации");
        }
    }
    private void checkAvailable(Item item){
        if(item.getAvailable().equals(false)){
            throw new ValidException("Ошибка валидации");
        }
    }
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository repository;
    private final ItemService itemService;
    private final UserService userService;

    public BookingDto postBooking(int id, BookingDtoRequest bookingDtoRequest){
        User user = UserMapper.fromUserDto(userService.getUserById(id));
        ItemDto itemDto = itemService.getItemId(bookingDtoRequest.getItemId());
        Item item = itemService.getItemNDto(bookingDtoRequest.getItemId());
        Booking booking = new Booking(bookingDtoRequest.getStart(),bookingDtoRequest.getEnd(),item,user,BookingStatus.WAITING);
        return BookingMapper.mapToBookingDto(repository.save(booking));
    }
}

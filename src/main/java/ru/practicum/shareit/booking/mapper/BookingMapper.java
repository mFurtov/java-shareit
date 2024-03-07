package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;
@UtilityClass
public class BookingMapper {
    public BookingDto mapToBookingDto(Booking booking) {

        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), ItemMapper.maToItemDto(booking.getItem()), UserMapper.mapToUserDto(booking.getBooker()), booking.getStatus());
    }

}

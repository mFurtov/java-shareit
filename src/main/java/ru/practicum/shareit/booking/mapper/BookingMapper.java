package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromItem;
import ru.practicum.shareit.booking.modul.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookingMapper {
    public BookingDto mapToBookingDto(Booking booking) {
        if (booking != null) {
            return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), ItemMapper.maToItemDto(booking.getItem()), UserMapper.mapToUserDto(booking.getBooker()), booking.getStatus());
        } else {
            throw new EntityNotFoundException("Объект не найден");
        }
    }

    public BookingDtoFromItem mapToBookingDtoFromItem(Booking booking) {
        if (booking != null) {
            return new BookingDtoFromItem(booking.getId(), booking.getStart(), booking.getEnd(), booking.getBooker().getId());
        } else {
            throw new EntityNotFoundException("Объект не найден");
        }
    }

    public static List<BookingDto> mapToListUserDto(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(mapToBookingDto(booking));
        }

        return result;
    }

}

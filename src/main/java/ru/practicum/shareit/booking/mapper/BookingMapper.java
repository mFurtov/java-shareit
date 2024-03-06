package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.modul.Booking;

@UtilityClass
public class BookingMapper {
    public BookingDto mapToBookingDto(Booking booking){
        return new BookingDto(booking.getId(), booking.getStart(),booking.getEnd(),booking.getItem(),booking.getBooker(),booking.getStatus());
    }

}

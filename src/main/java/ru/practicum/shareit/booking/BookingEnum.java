package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.ValidException;

import java.util.stream.Stream;

public enum BookingEnum {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingEnum getEnum(String status) {
        return Stream.of(BookingEnum.values()).filter(bookingEnum -> bookingEnum.name().equalsIgnoreCase(status)).findFirst().orElseThrow(() -> new ValidException("Unknown state: UNSUPPORTED_STATUS"));
    }
}

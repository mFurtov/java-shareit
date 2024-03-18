package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.Create;

import ru.practicum.shareit.booking.modul.StartAndBeforeValid;

import java.time.LocalDateTime;

@Data
@StartAndBeforeValid(groups = Create.class)
public class BookingDtoRequest {
    private int itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}

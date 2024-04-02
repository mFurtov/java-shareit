package ru.practicum.gateway.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.modul.StartAndBeforeValid;

import java.time.LocalDateTime;

@Data
@StartAndBeforeValid(groups = Create.class)
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoRequest {
    private int itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}

package ru.practicum.gateway.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.booking.validator.StartAndBeforeValid;

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

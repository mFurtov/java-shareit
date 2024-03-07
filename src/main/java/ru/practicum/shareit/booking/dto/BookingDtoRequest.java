package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.modul.DataValidEndtAnnotation;
import ru.practicum.shareit.booking.modul.DataValidStartAnnotation;
import ru.practicum.shareit.exception.ValidException;

import java.time.LocalDateTime;

@Data
public class BookingDtoRequest {
    int itemId;
    @DataValidStartAnnotation(groups = Create.class)
    LocalDateTime start;
    @DataValidEndtAnnotation(groups = Create.class)
    LocalDateTime end;
}

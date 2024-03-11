package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.modul.DataValidEndtAnnotation;
import ru.practicum.shareit.booking.modul.DataValidStartAnnotation;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private int id;
    @NotBlank(groups = Create.class)
    @DataValidStartAnnotation(groups = Create.class)
    private LocalDateTime start;
    @NotBlank(groups = Create.class)
    @DataValidEndtAnnotation(groups = Create.class)
    private LocalDateTime end;
    @NotBlank(groups = Create.class)
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;

}

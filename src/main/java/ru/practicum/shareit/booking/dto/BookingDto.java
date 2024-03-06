package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private int id;
    @NotBlank(groups = Create.class)
    private LocalDateTime start;
    @NotBlank(groups = Create.class)
    private LocalDateTime end;
    @NotBlank(groups = Create.class)
    private Item item;
    private User booker;
    private BookingStatus status;

    public BookingDto(int id, LocalDateTime start, LocalDateTime end, Item item, User booker, BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}

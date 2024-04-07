package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDtoFromItem {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int bookerId;

    public BookingDtoFromItem(int id, LocalDateTime start, LocalDateTime end, int bookerId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.bookerId = bookerId;
    }
}

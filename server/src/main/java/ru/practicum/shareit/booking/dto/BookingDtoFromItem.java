package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDtoFromItem booking = (BookingDtoFromItem) o;
        return id == booking.id &&
                bookerId == booking.bookerId &&
                start.equals(booking.start) &&
                end.equals(booking.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, bookerId);
    }
}

package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.modul.DataValidEndtAnnotation;
import ru.practicum.shareit.booking.modul.DataValidStartAnnotation;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class BookingDtoFromItem {
    int id;
    @NotBlank(groups = Create.class)
    @DataValidStartAnnotation(groups = Create.class)
    private LocalDateTime start;
    @NotBlank(groups = Create.class)
    @DataValidEndtAnnotation(groups = Create.class)
    private LocalDateTime end;
    @NotBlank(groups = Create.class)
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

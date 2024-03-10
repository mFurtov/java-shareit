package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromItem;

import javax.persistence.*;
import javax.validation.constraints.*;


@Data
public class ItemDto {
    private int id;
    @NotBlank(groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private BookingDtoFromItem lastBooking;
    private BookingDtoFromItem nextBooking;

    public ItemDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}

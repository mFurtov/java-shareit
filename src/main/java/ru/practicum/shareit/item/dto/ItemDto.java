package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDtoFromItem;

import javax.validation.constraints.*;
import java.util.List;


@Data
public class ItemDto {
    private int id;
    @Size(min = 1, max = 100, groups = Create.class)
    @NotBlank(groups = Create.class)
    private String name;
    @Size(min = 1, max = 500, groups = Create.class)
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private BookingDtoFromItem lastBooking;
    private BookingDtoFromItem nextBooking;

    private List<CommentDto> comments;

    public ItemDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}

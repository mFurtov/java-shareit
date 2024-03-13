package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoFromItem;

import java.util.List;
import java.util.Objects;


@Data
public class ItemDto {
    private int id;

    private String name;

    private String description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDto)) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(name, itemDto.name) && Objects.equals(description, itemDto.description) && Objects.equals(available, itemDto.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, available);
    }
}

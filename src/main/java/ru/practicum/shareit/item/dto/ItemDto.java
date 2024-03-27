package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoFromItem;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Data
public class ItemDto {
    private int id;

    private String name;

    private String description;
    private Boolean available;
    private BookingDtoFromItem lastBooking;
    private BookingDtoFromItem nextBooking;

    private List<CommentDto> comments;
    private int requestId;

    public ItemDto(int id, String name, String description, Boolean available, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
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

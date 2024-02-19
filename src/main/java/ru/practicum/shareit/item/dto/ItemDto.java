package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public ItemDto(String name, String description, Boolean available, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}

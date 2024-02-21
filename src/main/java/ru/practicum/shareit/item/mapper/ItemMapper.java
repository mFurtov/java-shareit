package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item fromItemDto(ItemDto item, User user) {
        return new Item(item.getName(), item.getDescription(), item.getAvailable(), user);
    }

}

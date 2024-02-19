package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Service
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getName(), item.getDescription(), item.getAvailable(), item.getRequest());
    }

    public Item fromItemDto(ItemDto item, User user) {
        return new Item(item.getName(), item.getDescription(), item.getAvailable(), user, item.getRequest());
    }

}

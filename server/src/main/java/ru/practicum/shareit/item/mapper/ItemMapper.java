package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemToRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemDto maToItemDto(Item item) {
        int requestId = (item.getRequest() != null) ? item.getRequest().getId() : 0;
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), requestId);
    }

    public ItemToRequestDto mapItemToRequstDto(Item item) {
        return new ItemToRequestDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getRequest().getId());
    }

    public List<ItemToRequestDto> mapToListRequstDto(Iterable<Item> items) {
        List<ItemToRequestDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(mapItemToRequstDto(item));
        }
        return result;
    }

    public Item mapFromItemDto(ItemCreateDto item, User user, ItemRequest itemRequest) {
        return new Item(item.getName(), item.getDescription(), item.getAvailable(), user, itemRequest);
    }

    public List<ItemDto> mapToListItemDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(maToItemDto(item));
        }
        return result;
    }


}

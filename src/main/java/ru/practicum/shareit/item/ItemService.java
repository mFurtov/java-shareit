package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItem(int id);

    ItemDto getItemId(int userId, int id);

    Item getItemNDto(int id);

    List<ItemDto> searchItems(String search);

    ItemDto postItem(int userId, ItemDto itemDto);

    ItemDto patchItem(int userId, int id, ItemDto itemDto);
    void setAvailable(Item item);


}

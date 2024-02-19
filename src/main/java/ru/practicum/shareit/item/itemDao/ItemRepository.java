package ru.practicum.shareit.item.itemDao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getItem(int id);

    Item getItemId(int id);

    List<Item> searchItems(String search);

    Item postItem(Item item);

    Item patchItem(int userId, int id, String name, String description, Boolean available);
}

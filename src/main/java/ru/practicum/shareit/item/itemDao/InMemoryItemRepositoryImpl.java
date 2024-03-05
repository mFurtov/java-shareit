package ru.practicum.shareit.item.itemDao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Service
@Slf4j
public class InMemoryItemRepositoryImpl implements InMemoryItemRepository {
    private Map<Integer, Item> itemList = new HashMap<>();
    private Map<Integer, List<Item>> userItemIndex = new LinkedHashMap<>();

    private int id = 1;

    public List<Item> getItem(int id) {
        if (userItemIndex.containsKey(id)) {
            return userItemIndex.get(id);
        } else {
            throw new EntityNotFoundException("Пользователь с id" + id + " не найден");
        }
    }


    public Item getItemId(int id) {
        return itemList.get(id);
    }

    public List<Item> searchItems(String search) {
        List<Item> itemSearch = new ArrayList<>();
        String searchLowerCase = search.toLowerCase();

        for (Item item : itemList.values()) {
            if ((item.getName().toLowerCase().contains(searchLowerCase) || item.getDescription().toLowerCase().contains(searchLowerCase))
                    && item.getAvailable() && !searchLowerCase.isEmpty()) {
                itemSearch.add(item);
            }
        }
        return itemSearch;
    }

    @Override
    public Item postItem(Item item) {
        item.setId(incrementId());
        itemList.put(item.getId(), item);
        final List<Item> items = userItemIndex.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        items.add(item);
        return item;
    }

    public Item patchItem(int userId, int id, String name, String description, Boolean available) {
        Item itemPatch = itemList.get(id);

        if (userId == itemPatch.getOwner().getId()) {
            if (name != null && !name.isBlank()) {
                itemPatch.setName(name);
            }
            if (description != null && !description.isBlank()) {
                itemPatch.setDescription(description);
            }
            if (available != null) {
                itemPatch.setAvailable(available);
            }
        } else {
            log.error("Указанный владелец не владелец вещи");
            throw new OwnerException("Доступ к предмету отсутствует, указан неверный владелец");
        }
        return itemPatch;
    }

    private int incrementId() {
        return id++;
    }
}

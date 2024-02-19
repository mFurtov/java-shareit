package ru.practicum.shareit.item.itemDao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private List<Item> itemList = new ArrayList<>();
    private int id = 1;

    public List<Item> getItem(int id) {
        List<Item> itemGet = new ArrayList<>();
        for (Item item : itemList) {
            if (item.getOwner().getId() == id) {
                itemGet.add(item);
            }
        }
        return itemGet;
    }

    public Item getItemId(int id) {
        Item itemGet = null;
        for (Item item : itemList) {
            if (item.getId() == id) {
                itemGet = item;
            }
        }
        return itemGet;
    }

    public List<Item> searchItems(@RequestParam String search) {
        List<Item> itemSearch = new ArrayList<>();
        for (Item item : itemList) {
            if ((item.getName().toLowerCase().contains(search.toLowerCase()) || item.getDescription().toLowerCase().contains(search.toLowerCase())) && item.getAvailable() == true && !search.isEmpty()) {
                itemSearch.add(item);
            }
        }
        return itemSearch;
    }

    @Override
    public Item postItem(Item item) {
        item.setId(incrementId());
        itemList.add(item);
        return item;
    }

    public Item patchItem(int userId, int id, String name, String description, Boolean available) {
        Item itemPatch = null;
        for (Item item : itemList) {
            if (item.getId() == id) {
                itemPatch = item;
            }
        }
        if (userId == itemPatch.getOwner().getId()) {
            if (name != null) {
                itemPatch.setName(name);
            }
            if (description != null) {
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

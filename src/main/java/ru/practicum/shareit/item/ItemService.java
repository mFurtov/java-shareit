package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public List<Item> getItem(int id) {
        return itemRepository.getItem(id);
    }

    public Item getItemId(int id) {
        return itemRepository.getItemId(id);
    }

    public List<Item> searchItems(@RequestParam String search) {
        return itemRepository.searchItems(search);
    }

    public Item postItem(int userId, ItemDto itemDto) {
        User user = userService.userGetId(userId);
        return itemRepository.postItem(itemMapper.fromItemDto(itemDto, user));
    }

    public Item patchItem(int userId, int id, String name, String description, Boolean available) {
        User user = userService.userGetId(userId);
        return itemRepository.patchItem(userId, id, name, description, available);
    }
}

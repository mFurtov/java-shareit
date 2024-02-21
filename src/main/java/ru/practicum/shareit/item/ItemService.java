package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;

    public List<ItemDto> getItem(int id) {
        List<ItemDto> itemDto = new ArrayList<>();
        List<Item> item = itemRepository.getItem(id);
        for (Item i : item) {
            itemDto.add(ItemMapper.toItemDto(i));
        }
        return itemDto;
    }

    public ItemDto getItemId(int id) {
        return ItemMapper.toItemDto(itemRepository.getItemId(id));
    }

    public List<ItemDto> searchItems(String search) {
        List<ItemDto> itemDto = new ArrayList<>();
        List<Item> item = itemRepository.searchItems(search);
        for (Item i : item) {
            itemDto.add(ItemMapper.toItemDto(i));
        }
        return itemDto;
    }

    public ItemDto postItem(int userId, ItemDto itemDto) {
        User user = UserMapper.fromUserDto(userService.userGetId(userId));
        return ItemMapper.toItemDto(itemRepository.postItem(ItemMapper.fromItemDto(itemDto, user)));
    }

    public ItemDto patchItem(int userId, int id, String name, String description, Boolean available) {
        User user = UserMapper.fromUserDto(userService.userGetId(userId));
        return ItemMapper.toItemDto(itemRepository.patchItem(userId, id, name, description, available));
    }
}

package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.InMemoryUserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
//    private final InMemoryUserService inMemoryUserService;
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

//    public ItemDto postItem(int userId, ItemDto itemDto) {
//        User user = UserMapper.fromUserDto(inMemoryUserService.userGetId(userId));
//        return ItemMapper.toItemDto(itemRepository.postItem(ItemMapper.fromItemDto(itemDto, user)));
//    }
//
//    public ItemDto patchItem(int userId, int id, ItemDto itemDto) {
//        User user = UserMapper.fromUserDto(inMemoryUserService.userGetId(userId));
//        return ItemMapper.toItemDto(itemRepository.patchItem(userId, id,itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable()));
//    }
}

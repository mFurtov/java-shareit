package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
//import ru.practicum.shareit.user.InMemoryUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository repository;
    @Override
    public List<ItemDto> getItem(int id) {
        return null;
    }

    @Override
    public ItemDto getItemId(int id) {
        return null;
    }

    @Override
    public List<ItemDto> searchItems(String search) {
        return null;
    }

    @Override
    public ItemDto postItem(int userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto patchItem(int userId, int id, ItemDto itemDto) {
        return null;
    }
//    private final InMemoryUserService inMemoryUserService;
//    private final ItemRepository itemRepository;
//
//    public List<ItemDto> getItem(int id) {
//        List<ItemDto> itemDto = new ArrayList<>();
//        List<Item> item = itemRepository.getItem(id);
//        for (Item i : item) {
//            itemDto.add(ItemMapper.toItemDto(i));
//        }
//        return itemDto;
//    }
//
//    public ItemDto getItemId(int id) {
//        return ItemMapper.toItemDto(itemRepository.getItemId(id));
//    }
//
//    public List<ItemDto> searchItems(String search) {
//        List<ItemDto> itemDto = new ArrayList<>();
//        List<Item> item = itemRepository.searchItems(search);
//        for (Item i : item) {
//            itemDto.add(ItemMapper.toItemDto(i));
//        }
//        return itemDto;
//    }

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

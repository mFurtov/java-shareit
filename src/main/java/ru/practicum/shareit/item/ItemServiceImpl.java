package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemDao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository repository;

    @Override
    public List<ItemDto> getItem(int id) {
        List<Item> item = repository.findByOwnerId(id);
        return ItemMapper.mapToListItemDto(item);
    }

    @Override
    public ItemDto getItemId(int id) {
        Item item = repository.getById(id);
        return ItemMapper.maToItemDto(item);
    }
    public Item getItemNDto(int id) {
        return repository.getById(id);
    }
    @PostMapping
    @Override
    public ItemDto postItem(int userId, ItemDto itemDto) {
        User user = UserMapper.fromUserDto(userService.getUserById(userId));
        return ItemMapper.maToItemDto(repository.save(ItemMapper.mapFromItemDto(itemDto, user)));
    }

    @Override
    public ItemDto patchItem(int userId, int id, ItemDto itemDto) {
        Item item = repository.getById(id);

        if (item.getOwner().getId() == userId) {
            if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
        } else {
            log.error("Указанный владелец не владелец вещи");
            throw new OwnerException("Доступ к предмету отсутствует, указан неверный владелец");
        }

        return ItemMapper.maToItemDto(repository.save(item));
    }

    public List<ItemDto> searchItems(String search) {
        return ItemMapper.mapToListItemDto(repository.search(search));
    }
}

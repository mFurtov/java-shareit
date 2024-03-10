package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int id) {
        List<ItemDto> itemList = itemService.getItem(id);
        log.info("Предметы пользователя \"{}\" выведены", id);
        return itemList;
    }

    @GetMapping("/{id}")
    public ItemDto getItemId(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId,@PathVariable int id) {
        ItemDto item = itemService.getItemId(userId,id);
        log.info("Предмет с id \"{}\" выведен", id);
        return item;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        List<ItemDto> itemList = itemService.searchItems(text);
        log.info("Поиск по запросу \"{}\" был выведен", text);
        return itemList;
    }

    @PostMapping
    public ItemDto postItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @Validated({Create.class}) @RequestBody ItemDto item) {
        ItemDto itemPost = itemService.postItem(userId, item);
        log.info("Предмет с id \"{}\" добавлен", itemPost.getId());
        return itemPost;
    }

    @PatchMapping("/{id}")
    public ItemDto patchItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @PathVariable int id, @RequestBody ItemDto itemRequest) {
        ItemDto item = itemService.patchItem(userId, id, itemRequest);
        log.info("Предмет с id \"{}\" обновлен", item.getId());
        return item;
    }

}

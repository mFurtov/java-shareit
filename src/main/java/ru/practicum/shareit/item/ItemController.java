package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getItem(@RequestHeader("X-Sharer-User-Id") int id) {
        List<Item> itemList = itemService.getItem(id);
        log.info("Предметы пользователя \"{}\" выведены", id);
        return itemList;
    }

    @GetMapping("/{id}")
    public Item getItemId(@PathVariable int id) {
        Item item = itemService.getItemId(id);
        log.info("Предмет с id \"{}\" выведен", id);
        return item;
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        List<Item> itemList = itemService.searchItems(text);
        log.info("Поиск по запросу \"{}\" был выведен", text);
        return itemList;
    }

    @PostMapping
    public Item postItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemDto item) {
        Item itemPost = itemService.postItem(userId, item);
        log.info("Предмет с id \"{}\" добавлен", itemPost.getId());
        return itemPost;
    }

    @PatchMapping("/{id}")
    public Item patchItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int id, @RequestBody ItemDto itemRequest) {
        Item item = itemService.patchItem(userId, id, itemRequest.getName(), itemRequest.getDescription(), itemRequest.getAvailable());
        log.info("Предмет с id \"{}\" обновлен", item.getId());
        return item;
    }
}

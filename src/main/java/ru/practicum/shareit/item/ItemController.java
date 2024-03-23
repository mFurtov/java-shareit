package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.pageable.PageableCreate;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {

        return itemService.findByOwnerId(userId, PageableCreate.getPageable(from, size, Sort.by(Sort.Direction.ASC, "id")));
    }

    @GetMapping("/{id}")
    public ItemDto getItemId(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @PathVariable int id) {
        ItemDto item = itemService.getItemId(userId, id);
        log.info("Предмет с id \"{}\" выведен", id);
        return item;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {
        List<ItemDto> itemList = itemService.searchItems(text, PageableCreate.getPageable(from, size, Sort.by(Sort.Direction.ASC, "id")));
        log.info("Поиск по запросу \"{}\" был выведен", text);
        return itemList;
    }

    @PostMapping
    public ItemDto postItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @Validated({Create.class}) @RequestBody ItemCreateDto item) {
        ItemDto itemPost = itemService.postItem(userId, item);
        log.info("Предмет с id \"{}\" добавлен", itemPost.getId());
        return itemPost;
    }

    @PatchMapping("/{id}")
    public ItemDto patchItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @PathVariable int id, @Validated({Update.class}) @RequestBody ItemCreateDto itemRequest) {
        ItemDto item = itemService.patchItem(userId, id, itemRequest);
        log.info("Предмет с id \"{}\" обновлен", item.getId());
        return item;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComments(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @PathVariable int itemId, @Validated({Create.class}) @RequestBody CommentRequestDto commentRequestDto) {
        CommentDto commentDto = itemService.postComments(userId, itemId, commentRequestDto);
        log.info("Комментарий с id \"{}\" доавлен", commentDto.getId());
        return commentDto;
    }

}

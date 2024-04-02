package ru.practicum.gateway.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.HeaderConstants;
import ru.practicum.gateway.Update;
import ru.practicum.gateway.item.client.ItemClient;
import ru.practicum.gateway.item.dto.CommentRequestDto;
import ru.practicum.gateway.item.dto.ItemCreateDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    @Autowired
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {
        return itemClient.getItem(userId,from,size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemId(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @PathVariable int id) {
        return itemClient.getItemId(userId,id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text, @RequestParam(defaultValue = "0") @PositiveOrZero int from, @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {

        return itemClient.searchItems(text,from,size);
    }

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @Validated({Create.class}) @RequestBody ItemCreateDto item) {

        return itemClient.postItem(userId,item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchItem(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @PathVariable int id, @Validated({Update.class}) @RequestBody ItemCreateDto itemRequest) {

        return itemClient.patchItem(userId,id,itemRequest);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComments(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @PathVariable int itemId, @Validated({Create.class}) @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.postComments(userId,itemId,commentRequestDto);
    }

}

package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItem(int id);

    ItemDto getItemId(int userId, int id);

    Item getItemNDto(int id);

    List<ItemDto> searchItems(String search);

    ItemDto postItem(int userId, ItemCreateDto itemDto);

    ItemDto patchItem(int userId, int id, ItemCreateDto itemDto);

    CommentDto postComments(int userId, int itemId, CommentRequestDto commentRequestDto);



}

package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.modul.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemRequestsMapper {
    public ItemRequest mapToItemRequest(ItemRequestCreateDto itemRequestCreateDto, User user) {
        return new ItemRequest(itemRequestCreateDto.getDescription(), user, LocalDateTime.now());
    }

    public ItemRequestDto mapFromItemRequest(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated());
    }
    public List<ItemRequestDto> mapFromItemRequest(Iterable<ItemRequest> itemRequest) {
        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest request : itemRequest) {
            result.add(mapFromItemRequest(request));
        }
        return result;
    }
}

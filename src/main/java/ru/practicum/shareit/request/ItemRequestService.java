package ru.practicum.shareit.request;


import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.modul.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto postRequest(int userId, ItemRequestCreateDto itemRequestCreateDto);
    ItemRequest getAllRequest(int id);
    List<ItemRequestDto> getRequest(int userId);
    List<ItemRequestDto> getAllRequest(int userId, int from, Integer size);
}

package ru.practicum.shareit.request;


import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestService {
    ItemRequestDto postRequest(int userId, ItemRequestCreateDto itemRequestCreateDto);
}

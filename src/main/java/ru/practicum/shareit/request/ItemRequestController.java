package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto postRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @Validated(Create.class) @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestService.postRequest(userId, itemRequestCreateDto);
    }

    @GetMapping
    public List<ItemRequestDto> getRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId){
        return itemRequestService.getRequest(userId);
    }
}

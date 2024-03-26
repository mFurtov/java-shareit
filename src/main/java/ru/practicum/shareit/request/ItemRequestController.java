package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.HeaderConstants;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto postRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @Validated(Create.class) @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        ItemRequestDto itemRequestDto = itemRequestService.postRequest(userId, itemRequestCreateDto);
        log.info("Добавлен запрос с id \"{}\" ", itemRequestDto.getId());
        return itemRequestDto;
    }

    @GetMapping
    public List<ItemRequestDto> getRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId) {
        List<ItemRequestDto> requests = itemRequestService.getRequest(userId);
        log.info("Выведен список запросов размером \"{}\" ", requests.size());
        return requests;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userId, @RequestParam(defaultValue = "0") @Min(0) int from, @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {
        List<ItemRequestDto> requests = itemRequestService.getAllRequestPage(userId, from, size);
        log.info("Выведен список запросов размером \"{}\" ", requests.size());
        return requests;
    }

    @GetMapping("/{id}")
    public ItemRequestDto getRequestById(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) int userI, @PathVariable int id) {
        ItemRequestDto requests = itemRequestService.getRequestById(userI, id);
        log.info("Выведен запрос c id \"{}\" ", requests.getId());
        return requests;
    }
}

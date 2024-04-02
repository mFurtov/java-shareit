package ru.practicum.gateway.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.HeaderConstants;
import ru.practicum.gateway.request.client.ItemRequestClient;
import ru.practicum.gateway.request.dto.ItemRequestCreateDto;

import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> postRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @Validated(Create.class) @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        return client.postRequest(userId,itemRequestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId) {
        return client.getRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequest(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userId, @RequestParam(defaultValue = "0") @Min(0) int from, @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return client.getAllRequest(userId,from,size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) Long userI, @PathVariable int id) {
        return client.getRequestById(userI, id);
    }
}

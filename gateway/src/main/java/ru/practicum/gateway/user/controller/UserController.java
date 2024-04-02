package ru.practicum.gateway.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.Update;
import ru.practicum.gateway.user.client.UserClient;
import ru.practicum.gateway.user.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient client;

    @GetMapping("/{id}")
    public ResponseEntity<Object> userGetUser(@PathVariable int id) {
        return client.getUserById(id);
    }

    @GetMapping()
    public ResponseEntity<Object> userGet() {

        return client.getAllUser();
    }

    @PostMapping
    public ResponseEntity<Object> postUser(@RequestBody UserDto user) {

        return client.postItem(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUser(@PathVariable int id, @Validated(Update.class) @RequestBody UserDto userRequest) {
        return client.patchUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public void dellUser(@PathVariable int id) {
        client.dellUser(id);
    }
}
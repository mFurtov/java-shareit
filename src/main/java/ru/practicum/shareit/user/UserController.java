package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public UserDto userGetUser(@PathVariable int id) {
        UserDto userDto = service.getUserById(id);
        log.info("Выведен пользователь с id \"{}\"", id);
        return userDto;
    }

    @GetMapping()
    public List<UserDto> userGet() {
        List<UserDto> userList = service.getUser();
        log.info("Выведен список пользователей. он равен \"{}\"", userList.size());
        return service.getUser();
    }

    @PostMapping
    public UserDto postUser(@Validated(Create.class) @RequestBody UserDto user) {
        UserDto userPost = service.save(user);
        log.info("Пользователь с id \"{}\" добавлен", userPost.getId());
        return userPost;
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable int id, @Validated(Update.class) @RequestBody UserDto userRequest) {
        UserDto userDto = service.patchUser(id, userRequest);
        log.info("Пользователь с id \"{}\" обновлен", userDto.getId());
        return userDto;
    }

    @DeleteMapping("/{id}")
    public void dellUser(@PathVariable int id) {
        service.deleteUser(id);
        log.info("Пользователь с id \"{}\" удален", id);
    }
}
package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> userGetAll() {
        List<UserDto> userList = userService.userGetAll();
        log.info("Выведен список пользователей. он равен \"{}\"", userList.size());
        return userList;
    }

    @GetMapping("/{id}")
    public UserDto userGetId(@PathVariable int id) {
        UserDto user = userService.userGetId(id);
        log.info("Выведен пользователь с id \"{}\"", id);
        return user;
    }

    @PostMapping
    public UserDto postUser(@Validated(Create.class) @RequestBody UserDto user) {
        UserDto userPost = userService.postUser(user);
        log.info("Пользователь с id \"{}\" добавлен", userPost.getId());
        return userPost;
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable int id, @RequestBody UserDto userRequest) {
        UserDto userPost = userService.patchUser(id, userRequest.getName(), userRequest.getEmail());
        log.info("Пользователь с id \"{}\" обновлен", userPost.getId());
        return userPost;
    }

    @DeleteMapping("/{id}")
    public void dellUser(@PathVariable int id) {
        userService.dellUser(id);
        log.info("Пользователь с id \"{}\" удален", id);
    }
}

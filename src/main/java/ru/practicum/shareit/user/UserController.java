package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> userGetAll() {
        List<User> userList = userService.userGetAll();
        log.info("Выведен список пользователей. он равен \"{}\"", userList.size());
        return userList;
    }

    @GetMapping("/{id}")
    public User userGetId(@PathVariable int id) {
        User user = userService.userGetId(id);
        log.info("Выведен пользователь с id \"{}\"", id);
        return user;
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        User userPost = userService.postUser(user);
        log.info("Пользователь с id \"{}\" добавлен", userPost.getId());
        return userPost;
    }

    @PatchMapping("/{id}")
    public User patchUser(@PathVariable int id, @RequestBody User userRequest) {
        User userPost = userService.patchUser(id, userRequest.getName(), userRequest.getEmail());
        log.info("Пользователь с id \"{}\" обновлен", userPost.getId());
        return userPost;
    }

    @DeleteMapping("/{id}")
    public void dellUser(@PathVariable int id) {
        userService.dellUser(id);
        log.info("Пользователь с id \"{}\" удален", id);
    }
}

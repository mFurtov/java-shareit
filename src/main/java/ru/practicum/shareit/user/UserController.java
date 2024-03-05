package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserServiceImpl service;

    @Transactional

        @GetMapping("/{id}")
    public UserDto userGetUser(@PathVariable int id){
        return service.getUserById(id);
    }
    @GetMapping()
    public List<UserDto> userGet() {
        return service.getUser();
    }

    @PostMapping
    public UserDto postUser(@Validated(Create.class) @RequestBody UserDto user) {
        UserDto userPost = service.saveUser(user);
        log.info("Пользователь с id \"{}\" добавлен", userPost.getId());
        return userPost;
    }

//    private final InMemoryUserService inMemoryUserService;
//
//    @GetMapping
//    public List<UserDto> userGetAll() {
//        List<UserDto> userList = inMemoryUserService.userGetAll();
//        log.info("Выведен список пользователей. он равен \"{}\"", userList.size());
//        return userList;
//    }
//
//    @GetMapping("/{id}")
//    public UserDto userGetId(@PathVariable int id) {
//        UserDto user = inMemoryUserService.userGetId(id);
//        log.info("Выведен пользователь с id \"{}\"", id);
//        return user;
//    }
//
//    @PostMapping
//    public UserDto postUser(@Validated(Create.class) @RequestBody UserDto user) {
//        UserDto userPost = inMemoryUserService.postUser(user);
//        log.info("Пользователь с id \"{}\" добавлен", userPost.getId());
//        return userPost;
//    }
//
//    @PatchMapping("/{id}")
//    public UserDto patchUser(@PathVariable int id, @Validated(Update.class) @RequestBody UserDto userRequest) {
//        UserDto userPost = inMemoryUserService.patchUser(id, userRequest.getName(), userRequest.getEmail());
//        log.info("Пользователь с id \"{}\" обновлен", userPost.getId());
//        return userPost;
//    }
//
//    @DeleteMapping("/{id}")
//    public void dellUser(@PathVariable int id) {
//        inMemoryUserService.dellUser(id);
//        log.info("Пользователь с id \"{}\" удален", id);
//    }
//    }

}
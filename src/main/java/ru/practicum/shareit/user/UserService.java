package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUser();

    UserDto getUserById(int id);

    UserDto save(UserDto userDto);

    void deleteUser(int userId);

    UserDto patchUser(int id, UserDto userRequest);

}

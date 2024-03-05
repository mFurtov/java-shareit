package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUser();
//    List<UserDto> getUserById(int id);
    UserDto saveUser(UserDto userDto);
    void deleteUser(long userId, long itemId);


}

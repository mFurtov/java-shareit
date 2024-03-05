package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {
    public static UserDto mapToListUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User fromUserDto(UserDto user) {
        return new User(user.getId(), user.getName(), user.getEmail());
    }

    public static List<UserDto> mapToListUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(mapToListUserDto(user));
        }

        return result;
    }
    public static User mapToNewUser(UserDto userDto) {
        User user = new User(userDto.getName(), userDto.getEmail());
        return user;
    }

}

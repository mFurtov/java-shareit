package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUser() {
        List<User> users = repository.findAll();
        return UserMapper.mapToListUserDto(users);
    }

    public UserDto getUserById(int id) {
        User users = repository.getById(id);
//        users.toString();
        return UserMapper.mapToUserDto(users);
    }


    @Override
    public UserDto save(UserDto userDto) {
        User user = repository.save(UserMapper.mapToNewUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUser(int userId) {
        repository.deleteById(userId);
    }

    @Override
    public UserDto patchUser(int id, UserDto userRequest) {
        User existingUser = repository.getById(id);
        if (userRequest.getName() != null) {
            existingUser.setName(userRequest.getName());
        }
        if (userRequest.getEmail() != null) {
            existingUser.setEmail(userRequest.getEmail());
        }
        User updatedUser = repository.save(existingUser);

        return UserMapper.mapToUserDto(updatedUser);
    }
}

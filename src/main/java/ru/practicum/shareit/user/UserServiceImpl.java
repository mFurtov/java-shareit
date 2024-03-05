package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    @Override
    public List<UserDto> getUser() {
        List<User> users = repository.findAll();
        return UserMapper.mapToListUserDto(users);
    }

    public UserDto getUserById(int id){
        User users = repository.getById(id);
        return UserMapper.mapToListUserDto(users);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
         User user = repository.save(UserMapper.mapToNewUser(userDto));
         return UserMapper.mapToListUserDto(user);
    }

    @Override
    public void deleteUser(long userId, long itemId) {

    }
}

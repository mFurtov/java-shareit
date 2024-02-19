package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRepositoryImpl implements UserRepository {
    private List<User> userList = new ArrayList<>();
    private int id = 1;

    @Override
    public List<User> userGetAll() {
        return userList;
    }

    @Override
    public User userGetId(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        throw new EntityNotFoundException("Пользователь с id" + id + " не найден");
    }

    @Override
    public User postUser(User user) {
        user.setId(incrementId());
        userList.add(user);
        return user;
    }


    @Override
    public User patchUser(int id, String name, String email) {
        User user = userGetId(id);
        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        return user;
    }

    @Override
    public void dellUser(int id) {
        User user = userGetId(id);
        userList.remove(user);
    }

    private int incrementId() {
        return id++;
    }
}

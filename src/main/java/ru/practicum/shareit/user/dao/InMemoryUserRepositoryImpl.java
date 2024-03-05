//package ru.practicum.shareit.user.dao;
//
//import org.springframework.stereotype.Service;
//import ru.practicum.shareit.exception.EntityNotFoundException;
//import ru.practicum.shareit.user.model.User;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class InMemoryUserRepositoryImpl implements InMemoryUserRepository {
//    private Map<Integer, User> userMap = new HashMap<>();
//    private int id = 1;
//
//    @Override
//    public List<User> userGetAll() {
//        return new ArrayList<>(userMap.values());
//    }
//
//    @Override
//    public User userGetId(int id) {
//        if (userMap.containsKey(id)) {
//            return userMap.get(id);
//        } else {
//            throw new EntityNotFoundException("Пользователь с id" + id + " не найден");
//        }
//    }
//
//    @Override
//    public User postUser(User user) {
//        user.setId(incrementId());
//        userMap.put(user.getId(),user);
//        return user;
//    }
//
//
//    @Override
//    public User patchUser(int id, String name, String email) {
//        User user = userGetId(id);
//        if (name != null && !name.isBlank()) {
//            user.setName(name);
//        }
//        if (email != null && !email.isBlank()) {
//            user.setEmail(email);
//        }
//        return user;
//    }
//
//    @Override
//    public void dellUser(int id) {
//        userMap.remove(id);
//    }
//
//    private int incrementId() {
//        return id++;
//    }
//}

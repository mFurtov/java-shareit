package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailUniqueException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public List<User> userGetAll() {
        return userRepository.userGetAll();
    }

    public User userGetId(int id) {
        return userRepository.userGetId(id);
    }

    public User postUser(User user) {
        checkEmail(user.getId(), user.getEmail());
        return userRepository.postUser(user);

    }

    public User patchUser(int id, String name, String email) {
        checkEmail(id, email);
        return userRepository.patchUser(id, name, email);
    }

    public void dellUser(int id) {
        userRepository.dellUser(id);

    }

    private boolean checkEmail(int id, String email) {
        List<User> userList = userRepository.userGetAll();
        boolean emailIsExist = false;
        for (User user : userList) {
            if (user.getEmail().equals(email) && id != user.getId()) {
                log.error("Ошибка почты, должна быть уникальной");
                throw new EmailUniqueException("Почта должна быть уникальной");
            } else {
                emailIsExist = false;
            }
        }
        return emailIsExist;
    }
}

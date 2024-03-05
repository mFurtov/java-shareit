//package ru.practicum.shareit.user;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import ru.practicum.shareit.exception.EmailUniqueException;
//import ru.practicum.shareit.user.dao.InMemoryUserRepository;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.mapper.UserMapper;
//import ru.practicum.shareit.user.model.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class InMemoryUserService {
//    private final InMemoryUserRepository inMemoryUserRepository;
//
//    public List<UserDto> userGetAll() {
//        List<UserDto> userDto = new ArrayList<>();
//        List<User> users = inMemoryUserRepository.userGetAll();
//        for (User u : users) {
//            userDto.add(UserMapper.mapToListUserDto(u));
//        }
//        return userDto;
//    }
//
//    public UserDto userGetId(int id) {
//        return UserMapper.mapToListUserDto(inMemoryUserRepository.userGetId(id));
//    }
//
//    public UserDto postUser(UserDto user) {
//        checkEmail(user.getId(), user.getEmail());
//        return UserMapper.mapToListUserDto(inMemoryUserRepository.postUser(UserMapper.fromUserDto(user)));
//
//    }
//
//    public UserDto patchUser(int id, String name, String email) {
//        checkEmail(id, email);
//        return UserMapper.mapToListUserDto(inMemoryUserRepository.patchUser(id, name, email));
//    }
//
//    public void dellUser(int id) {
//        inMemoryUserRepository.dellUser(id);
//
//    }
//
//    private boolean checkEmail(int id, String email) {
//        List<User> userList = inMemoryUserRepository.userGetAll();
//        boolean emailIsExist = false;
//        for (User user : userList) {
//            if (user.getEmail().equals(email) && id != user.getId()) {
//                log.error("Ошибка почты, должна быть уникальной");
//                throw new EmailUniqueException("Почта должна быть уникальной");
//            } else {
//                emailIsExist = false;
//            }
//        }
//        return emailIsExist;
//    }
//}

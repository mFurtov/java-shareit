package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
class UserServiceImplTest {
    private UserService userService;

    @Mock
    UserRepository repository;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(repository);
    }

    @Test
    void getUser() {
        User user = new User("testUser", "testUser@ya.ru");
        User userTwo = new User("testUser2", "testUser2@ya.ru");
        List<User> userList = List.of(user, userTwo);

        when(repository.findAll()).thenReturn(userList);

        List<UserDto> result = userService.getUser();
        verify(repository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(userList.size(), result.size());
        assertEquals(user.getEmail(), result.get(0).getEmail());

    }

    @Test
    void getUserById() {
        User user = new User(1, "testUser", "testUser@ya.ru");

        when(repository.getById(1)).thenReturn(user);

        UserDto result = userService.getUserById(1);
        verify(repository, times(1)).getById(1);
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());

    }

    @Test
    void save() {
        User user = new User(1, "testUser", "testUser@ya.ru");
        UserDto userDto = new UserDto(1, "testUser", "testUser@ya.ru");

        when(repository.save(any())).thenReturn(user);

        UserDto result = userService.save(userDto);
        verify(repository, times(1)).save(any());
        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void deleteUser() {
        userService.deleteUser(123);
        verify(repository, times(1)).deleteById(123);
    }

    @Test
    void patchUser() {
        User user = new User(1, "testUser", "testUser@ya.ru");
        UserDto userDtoName = new UserDto();
        userDtoName.setName("updateName");
        UserDto userDtoEmail = new UserDto();
        userDtoEmail.setEmail("updateEmail@ya.ru");

        when(repository.getById(anyInt())).thenReturn(user);
        when(repository.save(any())).thenReturn(user);

        assertEquals(userDtoName.getName(), userService.patchUser(1, userDtoName).getName());
        assertEquals(userDtoEmail.getEmail(), userService.patchUser(1, userDtoEmail).getEmail());

    }
}
package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
class UserServiceImplIntegrationTest {
    @Autowired
    UserService userService;

    @Test
    public void save() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@ya.ru");
        userDto.setName("test");
        UserDto user = userService.save(userDto);
        assertNotNull(user);
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getName(), user.getName());
    }

}
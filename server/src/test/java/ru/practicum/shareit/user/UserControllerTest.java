package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;


import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DirtiesContext
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void userGetUser() throws Exception {
        UserDto userDto = new UserDto(1, "Test", "test@ya.ru");
        when(userService.getUserById(anyInt())).thenReturn(userDto);

        mvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void userGet() throws Exception {
        List<UserDto> userList = List.of(
                new UserDto(1, "Test", "test@ya.ru"),
                new UserDto(2, "Test2", "test2@ya.ru")
        );
        when(userService.getUser()).thenReturn(userList);

        // Act & Assert
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(userList.size()));
    }

    @Test
    void postUser() throws Exception {
        UserDto userDto = new UserDto(1, "Test", "test@ya.ru");
        UserDto savedUserDto = new UserDto(1, "Test", "test@ya.ru");
        when(userService.save(any(UserDto.class))).thenReturn(savedUserDto);

        // Act & Assert
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUserDto.getId()))
                .andExpect(jsonPath("$.name").value(savedUserDto.getName()))
                .andExpect(jsonPath("$.email").value(savedUserDto.getEmail()));

    }

    @Test
    void patchUser() throws Exception {
        // Arrange
        UserDto userDto = new UserDto(1, "Test", "test@ya.ru");
        UserDto updatedUserDto = new UserDto(1, "TestUpdate", "updated@ya.ru");
        when(userService.patchUser(anyInt(), any(UserDto.class))).thenReturn(updatedUserDto);

        // Act & Assert
        mvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedUserDto.getName()))
                .andExpect(jsonPath("$.email").value(updatedUserDto.getEmail()));
    }

    @Test
    void dellUser() throws Exception {
        mvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk());
    }
}
package ru.practicum.gateway.user.dto;

import lombok.Data;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.Update;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private int id;
    @Size(min = 1, max = 100, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String name;
    @Size(min = 1, max = 500, groups = {Create.class, Update.class})
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String email;

    public UserDto(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

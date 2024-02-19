package ru.practicum.shareit.user.model;

import javax.validation.constraints.*;

import lombok.Data;


/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private int id;
    @NotEmpty
    private String name;

    @Email
    @NotNull
    private String email;
}

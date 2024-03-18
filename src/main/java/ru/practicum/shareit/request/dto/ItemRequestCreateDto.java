package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class ItemRequestCreateDto {

    @Size(min = 1, max = 100, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String description;
}

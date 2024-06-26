package ru.practicum.gateway.request.dto;

import lombok.Data;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class ItemRequestCreateDto {

    @Size(min = 1, max = 100, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String description;
}

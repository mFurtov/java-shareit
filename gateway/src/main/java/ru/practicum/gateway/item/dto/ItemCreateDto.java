package ru.practicum.gateway.item.dto;

import lombok.Data;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ItemCreateDto {
    @Size(min = 1, max = 100, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String name;
    @Size(min = 1, max = 500, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;

    private int requestId;
}

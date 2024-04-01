package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentRequestDto {
    @Size(min = 1, max = 1000, groups = Create.class)
    @NotBlank(groups = Create.class)
    private String text;
}

package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentMapper {
    public CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }

    public List<CommentDto> mapToListCommentDto(Iterable<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        for (Comment item : comments) {
            result.add(mapToCommentDto(item));
        }
        return result;
    }
}

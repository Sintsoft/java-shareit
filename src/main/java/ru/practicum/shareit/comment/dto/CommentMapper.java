package ru.practicum.shareit.comment.dto;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    private CommentMapper() {
    }

    public static Comment fromDto(RequestCommentDto commentDto, Item item, User user) {
        return new Comment(
                null,
                commentDto.getText(),
                item,
                user,
                LocalDateTime.now()
        );
    }

    public static NestedCommentDto toNested(Comment comment) {
        return new NestedCommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}

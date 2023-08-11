package ru.practicum.shareit.comment.dto;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment fromDTO(RequestCommentDTO commentDto, Item item, User user) {
        return new Comment(
                null,
                commentDto.getText(),
                item,
                user,
                LocalDateTime.now()
        );
    }

    public static NestedCommentDTO toNestedDTO(Comment comment) {
        return new NestedCommentDTO(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}

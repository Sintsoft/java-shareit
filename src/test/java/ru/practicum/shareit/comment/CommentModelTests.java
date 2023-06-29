package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.dto.NestedCommentDto;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.model.Comment;

import javax.validation.Validator;
import java.time.LocalDateTime;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

public class CommentModelTests {

    public static Validator validator = buildDefaultValidatorFactory().getValidator();

    private final LocalDateTime commentTime = LocalDateTime.now();

    @Test
    public void commentCreationTest() {
        Comment testComment = new Comment(
                null,
                "test comment text",
                TestDataGenerator.generateTestItem(1L, 1L),
                TestDataGenerator.generateTestUser(2L),
                commentTime
        );
        assertEquals(0, validator.validate(testComment).size());
    }

    @Test
    public void commentToDtoTest() {
        Comment testComment = new Comment(
                null,
                "test comment text",
                TestDataGenerator.generateTestItem(1L, 1L),
                TestDataGenerator.generateTestUser(2L),
                commentTime
        );
        testComment.setId(1L);
        NestedCommentDto testDTO = CommentMapper.toNested(testComment);
        assertEquals(1L, testDTO.getId());
        assertEquals("user2", testDTO.getAuthorName());
        assertEquals("test comment text", testDTO.getText());
        assertEquals(commentTime, testDTO.getCreated());
    }

    @Test
    public void commentFromDtoTest() {
        RequestCommentDto testDto = new RequestCommentDto("test comment text");
        Comment testComment = CommentMapper.fromDto(
                testDto,
                TestDataGenerator.generateTestItem(1L, 1L),
                TestDataGenerator.generateTestUser(2L));
        assertFalse(testComment.getCreated().isAfter(LocalDateTime.now()));
        assertEquals(TestDataGenerator.generateTestItem(1L, 1L), testComment.getItem());
        assertEquals(TestDataGenerator.generateTestUser(2L), testComment.getAuthor());
        assertEquals("test comment text", testComment.getText());
        assertNull(testComment.getId());
    }
}

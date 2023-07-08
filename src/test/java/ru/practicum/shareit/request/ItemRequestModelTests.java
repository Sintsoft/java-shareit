package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;
public class ItemRequestModelTests {

    public static Validator validator = buildDefaultValidatorFactory().getValidator();

    @Test
    void createRequestTest() {
        ItemRequest testRequest = new ItemRequest(
                null,
                "request description",
                LocalDateTime.now(),
                TestDataGenerator.generateTestUser(2L)
        );
        assertEquals(0, validator.validate(testRequest).size());
        assertEquals("request description", testRequest.getDescription());
        assertEquals(TestDataGenerator.generateTestUser(2L), testRequest.getRequestor());
        assertFalse(testRequest.getCreated().isAfter(LocalDateTime.now()));
        assertNull(testRequest.getId());
    }

    @Test
    void requestFromDto() {
        ItemRequest testRequest = ItemRequestMapper.fromDto(
                new RequestItemRequestDto("request description"),
                TestDataGenerator.generateTestUser(2L)
        );
        assertEquals(0, validator.validate(testRequest).size());
        assertEquals("request description", testRequest.getDescription());
        assertEquals(TestDataGenerator.generateTestUser(2L), testRequest.getRequestor());
        assertFalse(testRequest.getCreated().isAfter(LocalDateTime.now()));
        assertNull(testRequest.getId());
    }

    @Test
    void requestToDto() {
        ResponseItemRequestDto testDto = ItemRequestMapper.toDto(
                TestDataGenerator.generateTestItemRequest(1L, 2L), List.of());
        assertEquals("request1", testDto.getDescription());
        assertFalse(testDto.getCreated().isAfter(LocalDateTime.now()));
        assertEquals(0, testDto.getItems().size());

    }

}

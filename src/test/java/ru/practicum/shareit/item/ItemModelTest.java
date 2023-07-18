package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Validator;

import java.util.List;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

public class ItemModelTest {

    public static Validator validator = buildDefaultValidatorFactory().getValidator();

    @Test
    void createItemTest() {
        Item testItem = new Item(
                null,
                TestDataGenerator.generateTestUser(1L),
                "testitem",
                "testitem description",
                true,
                null
        );
        assertTrue(validator.validate(testItem).isEmpty());
        assertNull(testItem.getId());
        assertEquals(1L, testItem.getUser().getId());
        assertEquals("testitem", testItem.getName());
        assertEquals("testitem description", testItem.getDescription());
        assertTrue(testItem.getAvailable());
    }

    @Test
    void invalidItemValidationTest() {
        Item testItem = new Item(
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertFalse(validator.validate(testItem).isEmpty());
        assertEquals(3, validator.validate(testItem).size());
    }

    @Test
    void itemFromDtoTest() {
        RequestItemDto testDto = new RequestItemDto(
                "item1",
                "item1 description",
                true,
                null
        );

        Item testItem = ItemMapper.fromDto(testDto);
        assertTrue(validator.validate(testItem).isEmpty());
        assertNull(testItem.getId());
        assertEquals("item1", testItem.getName());
        assertEquals("item1 description", testItem.getDescription());
        assertTrue(testItem.getAvailable());
    }

    @Test
    void itemToDtoTest() {
        ResponseItemDto testDto = ItemMapper.toDto(
                TestDataGenerator.generateTestItem(1L, 1L), null, null, List.of());

        assertEquals(1L, testDto.getId());
        assertEquals("item1", testDto.getName());
        assertEquals("item1 description", testDto.getDescription());
        assertTrue(testDto.getAvailable());
        assertEquals(1L, testDto.getOwner());
        assertNull(testDto.getLastBooking());
        assertNull(testDto.getNextBooking());
        assertTrue(testDto.getComments().isEmpty());
        assertNull(testDto.getRequestId());
    }
}

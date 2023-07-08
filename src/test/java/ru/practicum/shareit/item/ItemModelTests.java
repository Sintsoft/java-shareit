package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

public class ItemModelTests {

    @Test
    void createItemTest() {
        // Without nested objects
        assertDoesNotThrow(
                () -> new Item(null, "test item", "test item description", false, null, null)
        );

        // With nested objects
        assertDoesNotThrow(
                () -> new Item(
                        null,
                        "test item",
                        "test item description",
                        false,
                        TestDataGenerator.generateTestUser(1L),
                        TestDataGenerator.generateTestItemRequest(1L, 2L)
        ));
    }

    @Test
    void createRequestItemDTOTest() {
        assertDoesNotThrow(
                () -> new RequestItemDTO("test item", "test item description", true, null)
        );
    }

    @Test
    void createResponseItemTest() {
        assertDoesNotThrow(
                () -> new ResponseItemDTO(1L, "test item", "test item description", false, null)
        );
    }

    @Test
    void mapItemFromDtoTest() {
        Item testItem = ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, 1L),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)
        );

        assertNull(testItem.getId());
        assertEquals("item1", testItem.getName());
        assertEquals("item1 description", testItem.getDescription());
        assertTrue(testItem.getAvailable());
        assertEquals(1L, testItem.getUser().getId());
        assertEquals(1L, testItem.getRequest().getId());
        assertEquals(2L, testItem.getRequest().getRequestor().getId());
    }

    @Test
    void mapItemToDtoTest() {
        ResponseItemDTO testDto = ItemMapper.toDto(TestDataGenerator.generateTestItem(1L, 1L, 1L, 2L));

        assertEquals(1L, testDto.getId());
        assertEquals("item1", testDto.getName());
        assertEquals("item1 description", testDto.getDescription());
        assertTrue(testDto.getAvailable());
        assertEquals(1L, testDto.getRequestId());
    }

    @Test
    void updateFromDtoTest() {
        Item testItem = TestDataGenerator.generateTestItem(1L, 1L, 1L, 2L)
                .updateFromDto(
                    new RequestItemDTO(null, null, null, null)
        );

        assertEquals(1L, testItem.getId());
        assertEquals("item1", testItem.getName());
        assertEquals("item1 description", testItem.getDescription());
        assertTrue(testItem.getAvailable());
        assertEquals(1L, testItem.getRequest().getId());

        testItem = testItem.updateFromDto(
                new RequestItemDTO("new name", "new description", false, 100L)
        );

        assertEquals(1L, testItem.getId());
        assertEquals("new name", testItem.getName());
        assertEquals("new description", testItem.getDescription());
        assertFalse(testItem.getAvailable());
        assertEquals(1L, testItem.getRequest().getId());
    }
}

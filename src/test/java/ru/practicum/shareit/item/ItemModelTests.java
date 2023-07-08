package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.item.dto.RequestItemDTO;
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


}

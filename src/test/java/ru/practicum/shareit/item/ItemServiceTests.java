package ru.practicum.shareit.item;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceTests {

    @Autowired
    ItemService testItemService;

    @Autowired
    UserService testUserService;

    @Test
    @Order(1)
    void addUserTests() {
        assertDoesNotThrow(() -> {
            testUserService.addUser(
                    new UserDto(null, "name", "email@mail.ru")
            );
        });
        assertEquals(1, testUserService.getAllUsers().size());
    }

    @Test
    @Order(2)
    void addItemTest() {
        assertDoesNotThrow(() -> {
            testItemService.addItem(
                    new ItemDto(
                            null,
                            "Предмет",
                            "Описание",
                            true,
                            1,
                            null
                    )
            );
        });
        assertEquals(1, testItemService.getAllItems().size());
    }

    @Test
    @Order(3)
    void updateItemTest() {
        assertDoesNotThrow(() -> {
            testItemService.updateItem(
                    1,
                    new ItemDto(
                            null,
                            null,
                            "Новое Описание",
                            true,
                            1,
                            null
                    )
            );
        });
        assertEquals("Новое Описание", testItemService.getItem(1).getDescription());
    }

    @Test
    @Order(4)
    void searhItemTest() {
        assertEquals(1, testItemService.searchItem("дмЕт").size());
    }
}

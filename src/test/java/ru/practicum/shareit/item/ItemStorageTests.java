package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.item.vault.ItemStorage;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemStorageTests {

    @Autowired
    ItemStorage testStorage;

    @MockBean
    ItemRepository mockedItemRepository;

    @BeforeEach
    void setUp() {
        when(mockedItemRepository.save(any())).thenThrow(new DataIntegrityViolationException("message"));
    }

    @Test
    void saveItemExceptionTest() {
        assertThrows(ShareItSQLExecutionFailed.class,
                () -> testStorage.createItem(
                        ItemMapper.fromDto(
                                TestDataGenerator.generateTestRequestItemDTO(1L, null),
                                TestDataGenerator.generateTestUser(1L), null)));
    }

    @Test
    void updateItemExceptionTest() {
        assertThrows(ShareItSQLExecutionFailed.class,
                () -> testStorage.updateItem(
                        TestDataGenerator.generateTestItem(1L, 1L, null, null)));
    }

    @Test
    void saveItemWithIdTest() {
        assertThrows(ShareItIvanlidEntity.class,
                () -> testStorage.createItem(
                        TestDataGenerator.generateTestItem(1L, 1L, null, null)));
    }

    @Test
    void updateItemWithIdTest() {
        assertThrows(ShareItIvanlidEntity.class,
                () -> testStorage.updateItem(
                        ItemMapper.fromDto(
                                TestDataGenerator.generateTestRequestItemDTO(1L, null),
                                TestDataGenerator.generateTestUser(1L), null)));
    }
}

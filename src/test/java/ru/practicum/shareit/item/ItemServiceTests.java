package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDTO;
import ru.practicum.shareit.item.dto.ResponseItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItUnalllowedAction;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor
public class ItemServiceTests {

    @Autowired
    ItemService testService;

    @MockBean
    ItemRepository mockedItemRepository;

    @MockBean
    UserRepository mockedUserRepository;

    @BeforeEach
    void setUpMocks() {
        when(mockedItemRepository.findById(anyLong())).thenReturn(Optional.empty());
        for (long i = 1L; i <= 10; i++) {
            when(mockedUserRepository.findById(i))
                    .thenReturn(Optional.of(TestDataGenerator.generateTestUser(i)));
            when(mockedItemRepository.save(
                    ItemMapper.fromDto(
                            TestDataGenerator.generateTestRequestItemDTO(i, i),
                            TestDataGenerator.generateTestUser(i),
                            null)))
                    .thenReturn(TestDataGenerator.generateTestItem(i, i, null, null));
            when(mockedItemRepository.save(
                            new Item(
                                    1L,
                                    "newitem1",
                                    "item1 description",
                                    false,
                                    TestDataGenerator.generateTestUser(i),
                                    null)))
                    .thenReturn(new Item(
                            1L,
                            "newitem1",
                            "item1 description",
                            false,
                            TestDataGenerator.generateTestUser(i),
                            null));
            when(mockedItemRepository.findById(i))
                    .thenReturn(Optional.of(TestDataGenerator.generateTestItem(i, i, null, null)));
        }
        when(mockedItemRepository.findUserItems(1L, 0,10))
                .thenReturn(List.of(TestDataGenerator.generateTestItem(1L, 1L, null, null)));
        when(mockedItemRepository.seachForItems("%Em1%", 0,10))
                .thenReturn(List.of(TestDataGenerator.generateTestItem(1L, 1L, null, null)));
    }

    @Test
    void createItemTest() {
        ResponseItemDTO testDto = testService.createItem(TestDataGenerator.generateTestRequestItemDTO(1L, null), 1L);

        assertEquals(1L, testDto.getId());
        assertEquals("item1", testDto.getName());
        assertEquals("item1 description", testDto.getDescription());
        assertTrue(testDto.getAvailable());
    }

    @Test
    void createNullItemTest() {
        assertThrows(ShareItIvanlidEntity.class,
                () -> testService.createItem(new RequestItemDTO(null, null, null, null), 1L));
    }

    @Test
    void updateItemTest() {
        testService.createItem(TestDataGenerator.generateTestRequestItemDTO(1L, null), 1L);
        ResponseItemDTO testDto = testService.updateItem(
                new RequestItemDTO("newitem1", null, false, null), 1L, 1L);

        assertEquals(1L, testDto.getId());
        assertEquals("newitem1", testDto.getName());
        assertEquals("item1 description", testDto.getDescription());
        assertFalse(testDto.getAvailable());
    }

    @Test
    void updateItemWrongUserTest() {
        assertThrows(ShareItUnalllowedAction.class,
                () -> testService.updateItem(new RequestItemDTO(null, null, null, null), 1L, 3L));
    }

    @Test
    void getItemTest() {
        ResponseItemDTO testDto = testService.getItem(1L);

        assertEquals(1L, testDto.getId());
        assertEquals("item1", testDto.getName());
        assertEquals("item1 description", testDto.getDescription());
        assertTrue(testDto.getAvailable());
    }

    @Test
    void getItemWrongIdTest() {
        assertThrows(ShareItEntityNotFound.class, () -> testService.getItem(100L));
    }

    @Test
    void getUserItemsTest() {
        assertEquals(1, testService.getUserItems(1L, 0, 10).size());
    }

    @Test
    void getUserItemsNegativeFromTest() {
        assertThrows(ShareItIvanlidEntity.class,
                () -> testService.getUserItems(1L, -1, 10));
    }

    @Test
    void searchItemTest() {
        assertEquals(1, testService.searchItems("Em1", 0, 10).size());
    }

    @Test
    void searchItemEmptiString() {
        assertEquals(0, testService.searchItems(" ", 0, 10).size());
    }
}

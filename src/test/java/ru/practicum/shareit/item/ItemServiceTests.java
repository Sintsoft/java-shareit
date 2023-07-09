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
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
                    ItemMapper.fromDto(
                            TestDataGenerator.generateTestRequestItemDTO(i, i),
                            TestDataGenerator.generateTestUser(i),
                            null).updateFromDto(
                                    new RequestItemDTO("newitem" + i, null, false, null))))
                    .thenReturn(TestDataGenerator.generateTestItem(i, i, null, null));
            when(mockedItemRepository.findById(i))
                    .thenReturn(Optional.of(TestDataGenerator.generateTestItem(i, i, null, null)));
        }
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

    }
}

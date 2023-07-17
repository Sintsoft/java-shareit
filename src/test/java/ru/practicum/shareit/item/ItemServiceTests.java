package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.booking.vault.BookingRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.vault.CommentRepository;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.request.vault.ItemRequestRepositry;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceTests {

    @Autowired
    ItemService testItemService;

    @MockBean
    ItemRepository mockItemRepo;

    @MockBean
    UserRepository mockerUserRepo;

    @MockBean
    BookingRepository mockBookingRepo;

    @MockBean
    ItemRequestRepositry mockItemRequestRepo;

    @MockBean
    CommentRepository mockCommentRepo;

    @BeforeEach
    void setUpUserRepo() {
        when(mockerUserRepo.findById(1L))
                .thenReturn(Optional.of(TestDataGenerator.generateTestUser(1L)));

        when(mockerUserRepo.findById(2L))
                .thenReturn(Optional.of(TestDataGenerator.generateTestUser(2L)));
    }

    @BeforeEach
    void setupItemRepo() {

        when(mockItemRepo.save(new Item(
                null,
                TestDataGenerator.generateTestUser(1L),
                "item1",
                "item1 description",
                true,
                null)))
                .thenReturn(TestDataGenerator.generateTestItem(1L, 1L));

        when(mockItemRepo.save(new Item(
                1L,
                TestDataGenerator.generateTestUser(1L),
                "newitem1",
                "item1 description",
                true,
                null)))
                .thenReturn(new Item(
                        1L,
                        TestDataGenerator.generateTestUser(1L),
                        "newitem1",
                        "item1 description",
                        true,
                        null));

        when(mockItemRepo.findById(1L))
                .thenReturn(Optional.of(TestDataGenerator.generateTestItem(1L, 1L)));

        when(mockItemRepo.findUserItems(TestDataGenerator.generateTestUser(1L)))
                .thenReturn(List.of(TestDataGenerator.generateTestItem(1L, 1L)));

        when(mockItemRepo.findByNameOrDescriptionContainingIgnoreCase("item1", "item1"))
                .thenReturn(List.of(TestDataGenerator.generateTestItem(1L, 1L)));
    }

    @BeforeEach
    void setUpBookingRepo() {
        when(mockBookingRepo.getItemLastBooking(1L))
                .thenReturn(List.of(TestDataGenerator.generateItemBooking(1L, 1L, 1L, 2L)));

        when(mockBookingRepo.getItemNextBooking(1L))
                .thenReturn(List.of(TestDataGenerator.generateItemBooking(2L, 1L, 1L, 3L)));

        when(mockBookingRepo.getUserBookings(2L, 0, Integer.MAX_VALUE))
                .thenReturn(List.of(
                        TestDataGenerator.generateItemBooking(1L, 1L, 1L, 2L)
                ));
    }

    @BeforeEach
    void setUpCommentRepo() {
        when(mockCommentRepo.save(
                new Comment(
                        null,
                        "text",
                        TestDataGenerator.generateTestItem(1L, 1L),
                        TestDataGenerator.generateTestUser(2L),
                        LocalDateTime.now()
                ))).thenReturn(new Comment(
                1L,
                "text",
                TestDataGenerator.generateTestItem(1L, 1L),
                TestDataGenerator.generateTestUser(2L),
                LocalDateTime.now()));
    }

    @Test
    void itemCreationTest() {
        ResponseItemDto testDto = testItemService.createItem(
                new RequestItemDto(
                        "item1",
                        "item1 description",
                        true,
                        null
                ),
                1L
        );
        assertEquals(1L, testDto.getId());
    }

    @Test
    void invalidItemCreationTest() {
        assertThrows(ShareItInvalidEntity.class, () -> {
            testItemService.createItem(
                new RequestItemDto(
                        null,
                        null,
                        null,
                        null
                ),
                1L);
        });
    }

    @Test
    void updateItemTest() {
        ResponseItemDto testDto = testItemService.updateItem(
                new RequestItemDto(
                        "newitem1",
                        "item1 description",
                        true,
                        null
                ),
                1L,
                1L
        );
        assertEquals("newitem1", testDto.getName());
        assertEquals(1L, testDto.getLastBooking().getId());
        assertEquals(2L, testDto.getNextBooking().getId());
    }

    @Test
    void findItemTest() {
        ResponseItemDto testDto = testItemService.getItem(1L, 1L);
        assertEquals(1L, testDto.getId());
        assertEquals("item1", testDto.getName());
        assertEquals("item1 description", testDto.getDescription());
    }

    @Test
    void findItemsTest() {
        List<ResponseItemDto> testDtos = testItemService.getUserItems(1L);
        assertEquals(1, testDtos.size());

    }

    @Test
    void searchItemTest() {
        List<ResponseItemDto> testDtos = testItemService.searchItem("item1");
        assertEquals(1, testDtos.size());
    }


}

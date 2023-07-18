package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.booking.vault.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.vault.ItemRequestRepositry;
import ru.practicum.shareit.user.vault.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemRequestServiceTests {

    @Autowired
    ItemRequestService testService;

    @MockBean
    ItemRepository mockItemRepo;

    @MockBean
    UserRepository mockerUserRepo;

    @MockBean
    BookingRepository mockBookingRepo;

    @MockBean
    ItemRequestRepositry mockItemRequestRepo;

    @BeforeEach
    void setUpRequestRepo() {
        when(mockItemRequestRepo.save(
                new ItemRequest(
                        null,
                        "request1",
                        any(),
                        TestDataGenerator.generateTestUser(2L))))
                .thenReturn(TestDataGenerator.generateTestItemRequest(1L, 2L));

        when(mockItemRequestRepo.findById(1L))
                .thenReturn(Optional.of(TestDataGenerator.generateTestItemRequest(1L, 2L)));

        when(mockItemRequestRepo.getOtherUserRequests(
                TestDataGenerator.generateTestUser(2L),
                PageRequest.of(0,10)))
                .thenReturn(Page.empty());

        when(mockItemRequestRepo.getUserRequests(
                TestDataGenerator.generateTestUser(2L)))
                .thenReturn(List.of(TestDataGenerator.generateTestItemRequest(1L, 2L)));
    }

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

    @Test
    void createRequestTest() {

        ResponseItemRequestDto testDto = testService.createRequest(
                new RequestItemRequestDto("request1"), 2L);
        assertEquals(1L, testDto.getId());
    }

    @Test
    void getRequestTest() {
        ResponseItemRequestDto testDto = testService.getRequest(1L, 2L);
        assertEquals(1L, testDto.getId());
        assertEquals("request1", testDto.getDescription());
    }

    @Test
    void getRequestsTest() {
        List<ResponseItemRequestDto> testDtos = testService.getRequests(0, 10, 2L);
        assertEquals(0, testDtos.size());

    }

    @Test
    void getUserRequestsTest() {
        List<ResponseItemRequestDto> testDtos = testService.getUserRequests(2L);
        assertEquals(1, testDtos.size());

    }
}

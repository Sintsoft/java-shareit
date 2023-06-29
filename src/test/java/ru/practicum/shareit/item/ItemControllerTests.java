package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.comment.dto.NestedCommentDto;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTests {

    @MockBean
    ItemService mockedItemService;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(mockedItemService.createItem(
                new RequestItemDto("item1", "item1 description", true, null), 1L))
                .thenReturn(ItemMapper.toDto(
                        TestDataGenerator.generateTestItem(1L, 1L),
                        null,
                        null,
                        List.of()));

        when(mockedItemService.updateItem(
                new RequestItemDto("newitem1", "item1 description", true, null),
                1L, 1L))
                .thenReturn(ItemMapper.toDto(
                        new Item(
                                1L,
                                TestDataGenerator.generateTestUser(1L),
                                "newitem1",
                                "item1 description",
                                true,
                                null),
                        TestDataGenerator.generateItemBooking(1L, 1L, 1L, 2L),
                        null,
                        List.of()));

        when(mockedItemService.getItem(1L, 1L))
                .thenReturn(ItemMapper.toDto(
                        TestDataGenerator.generateTestItem(1L, 1L),
                        TestDataGenerator.generateItemBooking(1L, 1L, 1L, 2L),
                        TestDataGenerator.generateItemBooking(2L, 1L, 1L, 3L),
                        List.of()
                ));

        when(mockedItemService.getUserItems(1L))
                .thenReturn(List.of(
                        ItemMapper.toDto(
                            TestDataGenerator.generateTestItem(1L, 1L),
                            TestDataGenerator.generateItemBooking(1L, 1L, 1L, 2L),
                            TestDataGenerator.generateItemBooking(2L, 1L, 1L, 3L),
                            List.of()),
                        ItemMapper.toDto(
                                TestDataGenerator.generateTestItem(2L, 1L),
                                TestDataGenerator.generateItemBooking(3L, 1L, 1L, 2L),
                                TestDataGenerator.generateItemBooking(4L, 1L, 1L, 3L),
                                List.of())
                ));

        when(mockedItemService.searchItem("item1"))
                .thenReturn(List.of(
                        ItemMapper.toDto(
                                TestDataGenerator.generateTestItem(1L, 1L),
                                TestDataGenerator.generateItemBooking(1L, 1L, 1L, 2L),
                                TestDataGenerator.generateItemBooking(2L, 1L, 1L, 3L),
                                List.of()),
                        ItemMapper.toDto(
                                TestDataGenerator.generateTestItem(2L, 1L),
                                TestDataGenerator.generateItemBooking(3L, 1L, 1L, 2L),
                                TestDataGenerator.generateItemBooking(4L, 1L, 1L, 3L),
                                List.of())
                ));

        when(mockedItemService.postComment(new RequestCommentDto("comment"), 1L, 2L))
                .thenReturn(new NestedCommentDto(1L, "comment", "user2", LocalDateTime.now()));
    }

    @Test
    void postItemTest() throws Exception {
        mockMvc.perform(post("/items")
                    .content(mapper.writeValueAsString(
                            new RequestItemDto("item1", "item1 description", true, null)
                    ))
                    .header("X-Sharer-User-Id", 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("item1"), String.class))
                .andExpect(jsonPath("$.description", is("item1 description"), String.class))
                .andExpect(jsonPath("$.available", is(true), Boolean.class));
    }

    @Test
    void patchItemTest() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(
                                new RequestItemDto(
                                        "newitem1",
                                        "item1 description",
                                        true,
                                        null)
                        ))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("newitem1"), String.class))
                .andExpect(jsonPath("$.description", is("item1 description"), String.class))
                .andExpect(jsonPath("$.available", is(true), Boolean.class));
    }

    @Test
    void getItemTest() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("item1"), String.class))
                .andExpect(jsonPath("$.description", is("item1 description"), String.class))
                .andExpect(jsonPath("$.available", is(true), Boolean.class))
                .andExpect(jsonPath("$.lastBooking.id", is(1L), Long.class));
    }

    @Test
    void getItemsTest() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void searchItemsTest() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text","item1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void postCommentTest() throws Exception {
        mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(
                                new RequestCommentDto("comment")
                        ))
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("comment"), String.class));
    }
}

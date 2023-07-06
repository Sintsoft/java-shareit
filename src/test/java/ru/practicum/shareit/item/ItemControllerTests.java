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
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
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
}

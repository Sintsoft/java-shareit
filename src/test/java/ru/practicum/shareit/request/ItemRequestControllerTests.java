package ru.practicum.shareit.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTests {

    @MockBean
    ItemRequestService mockedService;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(mockedService.createRequest(new RequestItemRequestDto("request1"), 2L))
                .thenReturn(ItemRequestMapper.toDto(
                        TestDataGenerator.generateTestItemRequest(1L, 2L),
                        List.of()));

        when((mockedService.getUserRequests(2L))).thenReturn(List.of());

        when((mockedService.getRequests(0,10, 2L))).thenReturn(List.of());

        when((mockedService.getRequest(1L, 2L))).thenReturn(ItemRequestMapper.toDto(
                TestDataGenerator.generateTestItemRequest(1L, 2L),
                List.of()));
    }

    @Test
    void postRequest() throws Exception {
        mockMvc.perform(post("/requests")
                .content(mapper.writeValueAsString(
                        new RequestItemRequestDto("request1")
                ))
                .header("X-Sharer-User-Id", 2L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void getUserRequests() throws Exception {
        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 2L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getRequests() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getRequest() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

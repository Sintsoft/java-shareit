package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.NestedItemDto;
import ru.practicum.shareit.user.dto.NestedUserDto;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTests {

    @MockBean
    BookingService mockedBookingService;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;


    private RequestBookingDto testInputDTO;

    private LocalDateTime testBookingStart = LocalDateTime.now().plusDays(1);
    private LocalDateTime testBookingEnd = LocalDateTime.now().plusDays(1).plusHours(1);

    @BeforeEach
    void setUp() {

        testInputDTO = new RequestBookingDto(
                1L,
                testBookingStart,
                testBookingEnd
        );
    }

    @Test
    void postCorrectBookingTest() throws Exception {
        ResponseBookingDto testOutputDTO = new ResponseBookingDto(
                1L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2L),
                BookingStatus.WAITING.toString()
        );
        when(mockedBookingService.createBooking(testInputDTO, 2L))
                .thenReturn(testOutputDTO);

        mockMvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(testInputDTO))
                        .header("X-Sharer-User-Id", 2L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(testOutputDTO.getId()), Long.class))
            .andExpect(jsonPath("$.item.id", is(1L), Long.class))
            .andExpect(jsonPath("$.booker.id", is(2L), Long.class));
    }

    @Test
    void postIncorrectUserBookingTest() throws Exception {
        ResponseBookingDto testOutputDTO = new ResponseBookingDto(
                1L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2L),
                BookingStatus.WAITING.toString()
        );
        when(mockedBookingService.createBooking(testInputDTO, 4L))
                .thenThrow(new ShareItEntityNotFound("No such user"));

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(testInputDTO))
                        .header("X-Sharer-User-Id", 4L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchBookingApprovedStatusTest() throws Exception {
        ResponseBookingDto testOutputDTO = new ResponseBookingDto(
                1L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2L),
                BookingStatus.APPROVED.toString()
        );
        when(mockedBookingService.approveBooking(1L, true, 1L))
                .thenReturn(testOutputDTO);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED"), String.class));
    }

    @Test
    void patchBookingRejectedStatusTest() throws Exception {
        ResponseBookingDto testOutputDTO = new ResponseBookingDto(
                1L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2L),
                BookingStatus.REJECTED.toString()
        );
        when(mockedBookingService.approveBooking(1L, false, 1L))
                .thenReturn(testOutputDTO);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "false")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("REJECTED"), String.class));
    }

    @Test
    void patchBookingWrongUserTest() throws Exception {
        when(mockedBookingService.approveBooking(1L, false, 100L))
                .thenThrow(new ShareItEntityNotFound("User not found"));

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 100L)
                        .param("approved", "false")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBooikngTest() throws Exception {
        ResponseBookingDto testOutputDTO = new ResponseBookingDto(
                1L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2L),
                BookingStatus.APPROVED.toString()
        );
        when(mockedBookingService.getBooking(1L, 2L))
                .thenReturn(testOutputDTO);

        mockMvc.perform(get("/bookings/1")
                .header("X-Sharer-User-Id", 2L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(testOutputDTO.getId()), Long.class))
            .andExpect(jsonPath("$.item.id", is(1L), Long.class))
            .andExpect(jsonPath("$.booker.id", is(2L), Long.class));
    }

    @Test
    void getWrongBooikngTest() throws Exception {
        when(mockedBookingService.getBooking(100L, 2L))
                .thenThrow(new ShareItEntityNotFound("No such booking"));

        mockMvc.perform(get("/bookings/100")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooikngTest() throws Exception {
        List<ResponseBookingDto> testList = List.of(new ResponseBookingDto(
                1L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2L),
                BookingStatus.APPROVED.toString()
        ), new ResponseBookingDto(
                2L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(3L),
                BookingStatus.WAITING.toString()
        ));

        when(mockedBookingService.getUserBookings(1L, "ALL", 0, 10))
                .thenReturn(testList);

        mockMvc.perform(get("/bookings")
                        .param("status", "ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getOwnerBooikngTest() throws Exception {
        List<ResponseBookingDto> testList = List.of(new ResponseBookingDto(
                1L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2L),
                BookingStatus.APPROVED.toString()
        ), new ResponseBookingDto(
                2L,
                new NestedItemDto(
                        1L,
                        "item",
                        1L,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(3L),
                BookingStatus.WAITING.toString()
        ));

        when(mockedBookingService.getUserItemsBookings(1L, "ALL", 0, 10))
                .thenReturn(testList);

        mockMvc.perform(get("/bookings/owner")
                        .param("status", "ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


}

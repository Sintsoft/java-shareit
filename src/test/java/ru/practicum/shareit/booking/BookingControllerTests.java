package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.NestedItemDto;
import ru.practicum.shareit.user.dto.NestedUserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.anyOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

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
                1l,
                testBookingStart,
                testBookingEnd
        );
    }

    @Test
    void postCorrectBookingTest() throws Exception {
        ResponseBookingDto testOutputDTO = new ResponseBookingDto(
                1l,
                new NestedItemDto(
                        1l,
                        "item",
                        1l,
                        "description",
                        true,
                        null
                ),
                testBookingStart,
                testBookingEnd,
                new NestedUserDto(2l),
                BookingStatus.WAITING.toString()
        );
        when(mockedBookingService.createBooking(testInputDTO, 2l))
                .thenReturn(testOutputDTO);

        mockMvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(testInputDTO))
                        .header("X-Sharer-User-Id", 2l)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(testOutputDTO.getId()), Long.class))
            .andExpect(jsonPath("$.item.id", is(1l), Long.class))
            .andExpect(jsonPath("$.booker.id", is(2l), Long.class));
    }

}

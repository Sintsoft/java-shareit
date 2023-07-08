package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {

    @MockBean
    UserService mockedUserService;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(mockedUserService.createUser(
                new UserDto(null, "user1", "user1@mail.ru")))
                .thenReturn(UserMapper.toDto(TestDataGenerator.generateTestUser(1L)));

        when(mockedUserService.updateUser(
                new UserDto(null, "newuser1", null), 1L))
                .thenReturn(new UserDto(1L, "newuser1", "user1@mail.ru"));

        when(mockedUserService.getUser(1L))
                .thenReturn(UserMapper.toDto(TestDataGenerator.generateTestUser(1L)));

        when(mockedUserService.getAllUsers())
                .thenReturn(List.of(
                        UserMapper.toDto(TestDataGenerator.generateTestUser(1L)),
                        UserMapper.toDto(TestDataGenerator.generateTestUser(2L))
                ));

        doNothing().when(mockedUserService).removeUser(1L);
        doThrow(new ShareItEntityNotFound("user not Found")).when(mockedUserService).removeUser(100L);
    }

    @Test
    void postUsertest() throws Exception {

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(
                                new UserDto(null, "user1", "user1@mail.ru")
                        ))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("user1"), String.class))
                .andExpect(jsonPath("$.email", is("user1@mail.ru"), String.class));

    }

    @Test
    void patchUsertest() throws Exception {

        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(
                                new UserDto(null, "newuser1", null)
                        ))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("newuser1"), String.class))
                .andExpect(jsonPath("$.email", is("user1@mail.ru"), String.class));

    }

    @Test
    void getUserTest() throws Exception {
        mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("user1"), String.class))
                .andExpect(jsonPath("$.email", is("user1@mail.ru"), String.class));;
    }

    @Test
    void getUsersTest() throws Exception {
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserWrongIdTest() throws Exception {
        mockMvc.perform(delete("/users/100")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

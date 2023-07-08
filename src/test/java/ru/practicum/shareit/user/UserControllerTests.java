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
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItUniqueValueCollision;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
    void setUpUserService() {
        for (long i = 1L; i <= 10L; i++) {
            when(mockedUserService.createUser(TestDataGenerator.generateTestRequestUserDTO(i)))
                    .thenReturn(TestDataGenerator.generateTestResponseUserDTO(i));
        }
        for (long i = 1L; i <= 10L; i++) {
            when(mockedUserService.findUserById(i))
                    .thenReturn(TestDataGenerator.generateTestResponseUserDTO(i));
        }

        when(mockedUserService.createUser(new RequestUserDTO("user2", "user1@email.com")))
                .thenThrow(new ShareItUniqueValueCollision("This email, already taken"));
        when(mockedUserService.createUser(new RequestUserDTO(null, null)))
                .thenThrow(new ShareItIvanlidEntity("Invalid user"));
        when(mockedUserService.updateUser(new RequestUserDTO("updateuser1", "user1@email.com"), 1L))
                .thenReturn(new ResponseUserDTO(1L, "updateuser1", "user1@email.com"));
        when(mockedUserService.updateUser(new RequestUserDTO(null, "user1@email.com"), 2L))
                .thenThrow(new ShareItUniqueValueCollision("This email, already taken"));
        when(mockedUserService.findUserById(100L))
                .thenThrow(new ShareItEntityNotFound("User not found"));
        when(mockedUserService.findAllUsers(0, 10)).thenReturn(List.of());
        doNothing().when(mockedUserService).deleteUser(1L);
    }


    @Test
    void postUserTest() throws Exception {
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(
                                TestDataGenerator.generateTestRequestUserDTO(1L)
                        ))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("user1"), String.class))
                .andExpect(jsonPath("$.email", is("user1@email.com"), String.class));
    }

    @Test
    void postUserTakenEmailTest() throws Exception {
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(
                                new RequestUserDTO("user2", "user1@email.com")
                        ))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void postUserNullFields() throws Exception {
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(
                                new RequestUserDTO(null, null)
                        ))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchUserTest() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(
                                new RequestUserDTO("updateuser1", "user1@email.com")
                        ))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("updateuser1"), String.class))
                .andExpect(jsonPath("$.email", is("user1@email.com"), String.class));
    }

    @Test
    void findUserTest() throws Exception {
        mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("user1"), String.class))
                .andExpect(jsonPath("$.email", is("user1@email.com"), String.class));
    }

    @Test
    void findUserWrongIdTest() throws Exception {
        mockMvc.perform(get("/users/100")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUsersTest() throws Exception {
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

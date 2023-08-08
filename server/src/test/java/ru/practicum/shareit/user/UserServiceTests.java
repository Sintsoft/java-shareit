package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;
import ru.practicum.shareit.utility.exceptions.ShareItUniqueValueCollision;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserService testService;

    @MockBean
    UserRepository mockedUserRepo;

    @BeforeEach
    void setUpUserRepoMock() {

        /*
        * Creation mocks
        */
        for (long i = 1L; i <= 10L; i++) {
            when(mockedUserRepo.save(new User(null, "user" + i, "user" + i + "@email.com")))
                .thenReturn(TestDataGenerator.generateTestUser(i));
        }
        when(mockedUserRepo.save(new User(null, "user2", "user1@email.com")))
                .thenThrow(new DataIntegrityViolationException(("conteins \"constraint\" substring")));
        when(mockedUserRepo.save(new User(null, "errorusername", "error@mail.ru")))
                .thenThrow(new DataIntegrityViolationException(("Some message")));

        /*
         * Update mocks
         */
        when(mockedUserRepo.save(new User(1L, "updateuser1", "user1@email.com")))
                .thenReturn(new User(1L, "updateuser1", "user1@email.com"));
        when(mockedUserRepo.save(new User(2L, "user2", "user1@email.com")))
                .thenThrow(new DataIntegrityViolationException(("conteins \"constraint\" substring")));

        /*
        * Read mocks
        */
        when(mockedUserRepo.findById(1L)).thenReturn(Optional.of(TestDataGenerator.generateTestUser(1L)));
        when(mockedUserRepo.findById(2L)).thenReturn(Optional.of(TestDataGenerator.generateTestUser(2L)));
        when(mockedUserRepo.findById(100L)).thenReturn(Optional.empty());
        when(mockedUserRepo.findByEmail("user1@email.com"))
                .thenReturn(List.of(TestDataGenerator.generateTestUser(1L)));
        List<User> tenUsers = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            tenUsers.add(TestDataGenerator.generateTestUser(i));
        }
        when(mockedUserRepo.findAllFromSize(0, 10))
                .thenReturn(tenUsers);

        doNothing().when(mockedUserRepo).delete(TestDataGenerator.generateTestUser(1L));

    }

    @Test
    void createNormalUserTest() {
        ResponseUserDTO testDto = testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));

        assertEquals(1L, testDto.getId());
        assertEquals("user1", testDto.getName());
        assertEquals("user1@email.com", testDto.getEmail());
    }

    @Test
    void createDuplicateEmailUserTest() {
        testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));

        assertThrows(ShareItUniqueValueCollision.class, () ->
                testService.createUser(new RequestUserDTO("user2", "user1@email.com")));
    }

    @Test
    void createNullUserTest() {

        assertThrows(ShareItSQLExecutionFailed.class, () ->
                testService.createUser(new RequestUserDTO("errorusername", "error@mail.ru")));
    }

    @Test
    void updateUserTest() {
        testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));
        ResponseUserDTO testDto = testService.updateUser(
                new RequestUserDTO("updateuser1", "user1@email.com"), 1L);

        assertEquals(1L, testDto.getId());
        assertEquals("updateuser1", testDto.getName());
        assertEquals("user1@email.com", testDto.getEmail());
    }

    @Test
    void updateDuplicateEmailUserTest() {
        testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));
        testService.createUser(TestDataGenerator.generateTestRequestUserDTO(2L));
        assertThrows(ShareItUniqueValueCollision.class, () ->
                testService.updateUser(new RequestUserDTO("user2", "user1@email.com"), 2L));
    }

    @Test
    void findUserByIdTest() {
        testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));
        ResponseUserDTO testDto = testService.findUserById(1L);

        assertEquals(1L, testDto.getId());
        assertEquals("user1", testDto.getName());
        assertEquals("user1@email.com", testDto.getEmail());
    }

    @Test
    void findUserByWrongIdTest() {
        assertThrows(ShareItEntityNotFound.class, () ->
                testService.findUserById(100L));
    }

    @Test
    void findAllUsersTest() {
        for (long i = 1L; i <= 10L; i++) {
            testService.createUser(
                    TestDataGenerator.generateTestRequestUserDTO(i)
            );
        }

        assertEquals(10, testService.findAllUsers(0, 10).size());
        for (ResponseUserDTO dto : testService.findAllUsers(0, 10)
             ) {
            assertNotNull(dto.getId());
        }
    }

    @Test
    void deleteUserTest() {
        testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));
        assertDoesNotThrow(() -> testService.deleteUser(1L));
    }

    @Test
    void deleteUserTestWrongId() {
        assertThrows(ShareItEntityNotFound.class, () -> testService.deleteUser(100L));
    }
}

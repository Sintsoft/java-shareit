package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.errorHandling.exceptions.ShareItInvalidEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserService testUserService;

    @MockBean
    UserRepository mockerUserRepo;

    @BeforeEach
    void setUp() {
        when(mockerUserRepo.findById(1L))
                .thenReturn(Optional.of(TestDataGenerator.generateTestUser(1L)));

        when(mockerUserRepo.findById(1000L))
                .thenThrow(new DataIntegrityViolationException("constraint [uq_user_email]"));

        when(mockerUserRepo.save(new User(null, "user1", "user1@mail.ru")))
                .thenReturn(TestDataGenerator.generateTestUser(1L));

        when(mockerUserRepo.save(new User(1L, "newuser1", "user1@mail.ru")))
                .thenReturn(new User(1L, "newuser1", "user1@mail.ru"));

        when(mockerUserRepo.findAll()).thenReturn(
                List.of(
                        TestDataGenerator.generateTestUser(1L),
                        TestDataGenerator.generateTestUser(2L)
                ));

        doNothing().when(mockerUserRepo).delete(TestDataGenerator.generateTestUser(1L));
        doThrow(new ShareItEntityNotFound("user not found"))
                .when(mockerUserRepo).delete(TestDataGenerator.generateTestUser(100L));
    }

    @Test
    void createUserTest() {
        UserDto testDto = testUserService.createUser(new UserDto(null, "user1", "user1@mail.ru"));
        assertEquals("user1", testDto.getName());
        assertEquals("user1@mail.ru", testDto.getEmail());
        assertEquals(1L, testDto.getId());
    }

    @Test
    void createExistingUserTest() {
        assertThrows(ShareItInvalidEntity.class, () -> {
            testUserService.createUser(new UserDto(1L, "user1", "user1@mail.ru"));
        });

    }

    @Test
    void createNullUserTest() {
        assertThrows(ShareItInvalidEntity.class, () -> {
            testUserService.createUser(new UserDto(null, null, null));
        });

    }

    @Test
    void createInvaildEmailUserTest() {
        assertThrows(ShareItInvalidEntity.class, () -> {
            testUserService.createUser(new UserDto(null, "user1", "userru"));
        });

    }

    @Test
    void updateUserTest() {
        UserDto testDto = testUserService.updateUser(new UserDto(null, "newuser1", null), 1L);
        assertEquals("newuser1", testDto.getName());
        assertEquals("user1@mail.ru", testDto.getEmail());
        assertEquals(1L, testDto.getId());
    }

    @Test
    void findUserTest() {
        UserDto testDto = testUserService.getUser(1L);
        assertEquals("user1", testDto.getName());
        assertEquals("user1@mail.ru", testDto.getEmail());
        assertEquals(1L, testDto.getId());
    }

    @Test
    void findWrongIdUserTest() {
        assertThrows(ShareItEntityNotFound.class, () -> {
            testUserService.getUser(100L);
        });
    }

    @Test
    void findAllUsersTest() {
        List<UserDto> testDto = testUserService.getAllUsers();
        assertEquals(2, testDto.size());
    }

    @Test
    void deleteUserTest() {
        assertDoesNotThrow(() -> {
            testUserService.removeUser(1L);
        });
    }

    @Test
    void deleteWrongIdUserTest() {
        assertThrows(ShareItEntityNotFound.class, () -> {
            testUserService.removeUser(100L);
        });
    }
}

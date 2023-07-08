package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.RequestUserDTO;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;
import ru.practicum.shareit.utility.exceptions.ShareItUniqueValueCollision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserService testService;

    @MockBean
    UserRepository mockedUserRepo;

    @BeforeEach
    void setUpUserRepoMock() {
        when(mockedUserRepo.save(new User(null, "user1", "user1@email.com")))
                .thenReturn(TestDataGenerator.generateTestUser(1L));

        when(mockedUserRepo.save(new User(null, "user2", "user1@email.com")))
                .thenThrow(new DataIntegrityViolationException(("conteins \"constraint\" substring")));

        when(mockedUserRepo.save(new User(null, "errorusername", "error@mail.ru")))
                .thenThrow(new DataIntegrityViolationException(("Some message")));


    }

    @Test
    void createNormalUserTest() {
        ResponseUserDto testDto = testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));

        assertEquals(1L, testDto.getId());
        assertEquals("user1", testDto.getName());
        assertEquals("user1@email.com", testDto.getEmail());
    }

    @Test
    void createDuplicateEmailUserTest() {
        ResponseUserDto testDto = testService.createUser(TestDataGenerator.generateTestRequestUserDTO(1L));

        assertThrows(ShareItUniqueValueCollision.class, () -> {
            testService.createUser(new RequestUserDTO("user2", "user1@email.com"));
        });
    }

    @Test
    void createNullUserTest() {

        assertThrows(ShareItSQLExecutionFailed.class, () -> {
            testService.createUser(new RequestUserDTO("errorusername", "error@mail.ru"));
        });
    }

    @Test
    void createInvailUserTest() {
        assertThrows(ShareItIvanlidEntity.class, () -> {
            testService.createUser(new RequestUserDTO("  ", "not_a_email"));
        });
    }

}

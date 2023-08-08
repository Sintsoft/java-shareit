package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.user.vault.UserStorage;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserStorageTests {

    /*
    * Тесыт для доп покрытия
    */

    @Autowired
    UserStorage testStorage;

    @MockBean // Мы не будем рабоать с базой, прото мокаем
    UserRepository mockedUserRepo;

    @Test
    void sameEmailUpdateTest() {
        when(mockedUserRepo.save(
                UserMapper.fromDto(TestDataGenerator.generateTestRequestUserDTO(1L))))
            .thenReturn(TestDataGenerator.generateTestUser(1L));
        when(mockedUserRepo.save(new User(1L, "updateUserName", "user1@email.com")))
            .thenReturn(TestDataGenerator.generateTestUser(1L));
        when(mockedUserRepo.findById(1L))
                .thenReturn(Optional.of(TestDataGenerator.generateTestUser(1L)));


        User testUser = testStorage.createUser(
                UserMapper.fromDto(TestDataGenerator.generateTestRequestUserDTO(1L)));

        testUser.setName("updateUserName");
        testStorage.updateUser(testUser);

    }

    @Test
    void nullExceptionMessageInCreate() {
        when(mockedUserRepo.save(new User(null, "test", "test@test.test")))
                .thenThrow(new DataIntegrityViolationException(null));

        assertThrows(ShareItSQLExecutionFailed.class, () ->
                testStorage.createUser(new User(null, "test", "test@test.test")));
    }

    @Test
    void nullExceptionMessageInUpdate() {
        when(mockedUserRepo.save(new User(1L, "test", "test@test.test")))
                .thenThrow(new DataIntegrityViolationException(null));
        when(mockedUserRepo.findById(1L)).thenReturn(Optional.of(new User(1L, "test", "test@test.test")));

        assertThrows(ShareItSQLExecutionFailed.class, () ->
                testStorage.updateUser(new User(1L, "test", "test@test.test")));
    }
}

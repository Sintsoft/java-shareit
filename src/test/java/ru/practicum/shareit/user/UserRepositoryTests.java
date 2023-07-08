package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.vault.UserRepository;
import ru.practicum.shareit.utility.configuration.PersistenceConfig;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import({PersistenceConfig.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:reset_db.sql")
public class UserRepositoryTests {

    @Autowired
    UserRepository testRepo;

    @Autowired
    private TestEntityManager testEM;

    @Test
    void saveUserTest() {
        User testUser = new User(null, "user1", "user1@email.com");
        log.info("saveUserTest. Created test user - " + testUser);

        assertNull(testUser.getId());

        testUser = testRepo.save(testUser);

        assertEquals(1L, testUser.getId());
        assertEquals(TestDataGenerator.generateTestUser(1L), testUser);
    }

    @Test
    void saveUserWithTakenEmailTest() {
        log.info("saveUserWithTakenEmailTest. Saving test user");
        User testUser = testRepo.save(new User(null, "user1", "user1@email.com"));

        assertEquals(1L, testUser.getId());

        Throwable testException = assertThrows(DataIntegrityViolationException.class, () -> {
                testRepo.save(new User(null, "newuser1", "user1@email.com"));
            });
        assertNotNull(testException.getMessage());

        log.info("saveUserWithTakenEmailTest. Catched excption. Message = " + testException.getMessage());
        assertTrue(testException.getMessage().toLowerCase().contains("constraint"));
    }

    @Test
    void findUserByIdTest() {
        log.info("findUserByIdTest. Saving test user");
        testRepo.save(new User(null, "user1", "user1@email.com"));

        assertDoesNotThrow(() -> {
            testRepo.findById(1L).orElseThrow(() -> {
                throw new ShareItEntityNotFound("User not found");
            });
        });

        User testUser = testRepo.findById(1L).get();
        assertEquals(1L, testUser.getId());
        assertEquals("user1", testUser.getName());
        assertEquals("user1@email.com", testUser.getEmail());
    }

    @Test
    void findUserByEmailTest() {
        log.info("findUserByIdTest. Saving test user");
        testRepo.save(new User(null, "user1", "user1@email.com"));

        assertDoesNotThrow(() -> {
            testRepo.findByEmail("user1@email.com");
        });

        List<User> testUserResult = testRepo.findByEmail("user1@email.com");
        assertEquals(1, testUserResult.size());
        assertEquals("user1@email.com", testUserResult.get(0).getEmail());
    }

    @Test
    void findUserbyWrongIdTest() {
        assertTrue(testRepo.findById(10L).isEmpty());
    }


    @Test
    void updateUserTest() {
        log.info("updateUserTest. Saving test user");
        User testUser = testRepo.save(new User(null, "user1", "user1@email.com"));
        assertEquals(1L, testUser.getId());

        testUser.setName("newuser1");
        testUser = testRepo.save(testUser);

        assertEquals(1L, testUser.getId());
        assertEquals("newuser1", testUser.getName());
        assertEquals("user1@email.com", testUser.getEmail());

    }

    @Test
    void finadFromSizeTest() {
        for (long i = 1L; i <= 10L; i++) {
            testRepo.save(
                    UserMapper.fromDto(
                            TestDataGenerator.generateTestRequestUserDTO(i)
                    )
            );
        }

        assertEquals(10, testRepo.findAllFromSize(0, 100).size());
        assertEquals(1, testRepo.findAllFromSize(2, 1).size());
        assertEquals(7, testRepo.findAllFromSize(3, 10).size());
        assertEquals(0, testRepo.findAllFromSize(15, 10).size());
    }

    @Test
    void deleteUserTest() {
        log.info("updateUserTest. Saving test user");
        User testUser = testRepo.save(new User(null, "user1", "user1@email.com"));
        testRepo.delete(testUser);
        assertTrue(testRepo.findById(1L).isEmpty());
    }
}

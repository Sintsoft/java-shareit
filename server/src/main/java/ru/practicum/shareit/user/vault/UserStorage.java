package ru.practicum.shareit.user.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;
import ru.practicum.shareit.utility.exceptions.ShareItUniqueValueCollision;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;

//import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStorage {

    @Autowired
    private final UserRepository repository;

    public User createUser(User newUser) {
        log.trace("LEVEL: Storage. METHOD: createUser. INPUT: " + newUser);
        try {
            if (newUser.getId() != null) {
                throw new ShareItIvanlidEntity("New user must not have id");
            }
            return saveIfVaild(newUser);
        } catch (DataAccessException ex) {
            log.info("SQL exception!");
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
                log.info("Email collision during creation. Email - " + newUser.getEmail());
                throw new ShareItUniqueValueCollision("This email is already taken");
            }
            throw new ShareItSQLExecutionFailed("Failed to save new user due to: " + ex.getMessage());
        }
    }

    public User updateUser(User updUser) {
        log.trace("LEVEL: Storage. METHOD: createUser. INPUT: " + updUser);
        try {
            if (updUser.getId() == null) {
                throw new ShareItIvanlidEntity("Existing user must have id");
            }
            User oldUser = readUserById(updUser.getId());
            if (!oldUser.getEmail().equals(updUser.getEmail())
                    && !repository.findByEmail(updUser.getEmail()).isEmpty()) { // Не на всех БД constraint работает при update
                throw new ShareItUniqueValueCollision("This email is already taken");
            }
            return saveIfVaild(updUser);
        } catch (DataAccessException ex) {
            throw new ShareItSQLExecutionFailed("Failed to update user due to: " + ex.getMessage());
        }
    }

    public User readUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ShareItEntityNotFound("User with id = " + userId + " not found"));
    }

    public List<User> readManyUsers(int from, int size) {
        return repository.findAllFromSize(from, size);
    }

    public void deleteUser(Long userId) {
        repository.delete(readUserById(userId));
    }

    private User saveIfVaild(User savedUser) {
        return repository.save(savedUser);
    }
}

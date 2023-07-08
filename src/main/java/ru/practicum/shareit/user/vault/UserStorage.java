package ru.practicum.shareit.user.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItEntityNotFound;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;
import ru.practicum.shareit.utility.exceptions.ShareItUniqueValueCollision;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.stream.Collectors;

import static javax.validation.Validation.buildDefaultValidatorFactory;

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
        } catch (DataIntegrityViolationException ex) {
            log.info("Email collision during creation. Email - " + newUser.getEmail());
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
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
            } else if (!repository.findByEmail(updUser.getEmail()).isEmpty()) { // Не на всех БД constraint работает при update
                throw new ShareItUniqueValueCollision("This email is already taken");
            }
            return saveIfVaild(updUser);
        } catch (DataIntegrityViolationException ex) {
            log.info("Email collision during creation. Email - " + updUser.getEmail());
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
                throw new ShareItUniqueValueCollision("This email is already taken");
            }
            throw new ShareItSQLExecutionFailed("Failed to save new user due to: " + ex.getMessage());
        }
    }

    public User readUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ShareItEntityNotFound("User with id = " + userId + " not found"));
    }

    private User saveIfVaild(User savedUser) {
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            if (!validator.validate(savedUser).isEmpty()) {
                throw new ShareItIvanlidEntity("User parametrs invalid: "
                        + validator.validate(savedUser).stream().map(
                        violation -> "Поле "
                                + violation.getPropertyPath().toString()
                                + " " + violation.getMessage()
                ).collect(Collectors.joining(", ")));
            }
            return repository.save(savedUser);
        }
    }
}

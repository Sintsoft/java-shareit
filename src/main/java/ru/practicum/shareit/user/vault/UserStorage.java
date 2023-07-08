package ru.practicum.shareit.user.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;
import ru.practicum.shareit.utility.exceptions.ShareItSQLExecutionFailed;
import ru.practicum.shareit.utility.exceptions.ShareItUniqueValueCollision;

import javax.validation.Validator;
import java.util.stream.Collectors;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStorage {

    @Autowired
    private final UserRepository repository;

    private final Validator validator = buildDefaultValidatorFactory().getValidator();

    public User createUser(User newUser) {
        log.trace("LEVEL: Storage. METHOD: createUser. INPUT: " + newUser);
        try {
            return safeIfVaild(newUser);
        } catch (DataIntegrityViolationException ex) {
            log.info("Email collision during creation. Email - " + newUser.getEmail());
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
                throw new ShareItUniqueValueCollision("This email is already taken");
            }
            throw new ShareItSQLExecutionFailed("Failed to save new user due to: " + ex.getMessage());
        }
    }

    private User safeIfVaild(User savedUser) {
        if (!validator.validate(savedUser).isEmpty()) {
            throw new ShareItIvanlidEntity("User parametrs invalid: "
                    +  String.join(", ", validator.validate(savedUser).stream().map(
                    violation -> {
                        return  "Поле "
                                + violation.getPropertyPath().toString()
                                + " " + violation.getMessage();
                    }
            ).collect(Collectors.toList())));
        }
        return repository.save(savedUser);
    }
}
